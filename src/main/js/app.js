'use strict';

import {
    Col,
    Container,
    Tab,
    Tabs,
    Navbar,
    Nav,
    NavDropdown,
    Form,
    FormControl,
    Button,
    Row,
    ListGroup,
    OverlayTrigger,
    Popover,
    InputGroup,
    ProgressBar,
    ButtonGroup, Table, Card, Modal
} from 'react-bootstrap';
import axios from 'axios';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const follow = require('./follow'); // function to hop multiple links by "rel"
const stompClient = require('./websocket-listener');

const root = '/api';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {downloads: [], attributes: [], pageSize: 99, links: {}};
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.handleSocketCall = this.handleSocketCall.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    // componentDidCatch(error, info) {
    //     // Display fallback UI
    //     this.setState({ hasError: true });
    //     // You can also log the error to an error reporting service
    //     console.log(error, info);
    // }

    // tag::follow-2[]
    loadFromServer(pageSize) {

        axios.get('http://localhost:8080/data/downloads/')
            .then((response) => {
                this.setState({
                    downloads: response.data
                });
            })
            .catch( (error) => {
                console.log(error);
            });

        // follow(client, root, [
        //     {rel: 'downloads', params: {size: pageSize}}]
        // ).then(downloadCollection => {
        //     return client({
        //         method: 'GET',
        //         path: downloadCollection.entity._links.profile.href,
        //         headers: {'Accept': 'application/schema+json'}
        //     }).then(schema => {
        //         this.schema = schema.entity;
        //         return downloadCollection;
        //     });
        // }).done(downloadCollection => {
        //     this.setState({
        //         downloads: downloadCollection.entity._embedded.downloads,
        //         attributes: Object.keys(this.schema.properties),
        //         pageSize: pageSize,
        //         links: downloadCollection.entity._links
        //     });
        // });
    }

    // end::follow-2[]

    // tag::create[]

    onCreate(newDownload) {

        axios
            .post('http://localhost:8080/data/downloads/', newDownload)
            .then((response) => {

                if (response.status.toString() != '200') {

                    alert("there was an error!");

                } else {

                    this.handleClose();
                }
            });

    }

    // end::create[]

    // tag::delete[]
    onDelete(user) {
        client({method: 'DELETE', path: user._links.self.href}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    // end::delete[]

    handleSocketCall(responseObj){

        let message = JSON.parse(responseObj.body);
        let route = responseObj.headers.destination;

        switch (route) {

            case '/topic/newDownload':

                console.log("new download added to list")
                console.log(message);
                break;

            case '/topic/updateDownload':

                console.log("updated downloads")
                console.log(message);
                console.log(this.state.downloads);

                var download =  this.state.downloads.find((element) => {
                    return element.id === message.id;
                });

                console.log(download);

                break;

            case '/topic/deleteDownload':

                console.log("removed  deleted downloads")
                console.log(message);
                break;

            default:


        }


    }



    // tag::follow-1[]
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        stompClient.register([
            {route: '/topic/newDownload', callback: this.handleSocketCall},
            {route: '/topic/updateDownload', callback: this.handleSocketCall},
            {route: '/topic/deleteDownload', callback: this.handleSocketCall}
        ]);
    }

    // end::follow-1[]

    handleShow() {
        this.setState({show: true});
    }

    handleClose() {

        this.setState({show: false});
    }

    render() {

        return (
            <React.Fragment>
                <Navbar bg="dark" expand="lg">
                    <Navbar.Brand href="#home">XDCC Loader</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto">
                            {/*<Nav.Link href="">Home</Nav.Link>*/}
                            <NavDropdown title="Create new..." id="basic-nav-dropdown">
                                <NavDropdown.Item onClick={this.handleShow}>Download</NavDropdown.Item>
                                <NavDropdown.Item onClick={() => alert("creating channel")}>Channel</NavDropdown.Item>
                                <NavDropdown.Item onClick={() => alert("creating Server")}>Server</NavDropdown.Item>
                                <NavDropdown.Item onClick={() => alert("creating User")}>User</NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <Container fluid>
                    <Row>
                        <Col md={4}>
                            <p>Left Column</p>
                        </Col>
                        <Col md={8} className={"right-column"}>
                            <Tabs defaultActiveKey="activeDownloads" id="uncontrolled-tab-example">
                                <Tab eventKey="activeDownloads" title="Active Downloads">
                                    <DownloadList downloads={this.state.downloads}
                                                  onDelete={this.onDelete}
                                                  updatePageSize={this.updatePageSize}/>
                                </Tab>
                                <Tab eventKey="completedDownloads" title="Completed">
                                <DownloadList downloads={this.state.downloads}
                                                  onDelete={this.onDelete}
                                                  updatePageSize={this.updatePageSize}/>
                                </Tab>
                                <Tab eventKey="failedDownloads" title="Failed">
                                <DownloadList downloads={this.state.downloads}
                                                  onDelete={this.onDelete}
                                                  updatePageSize={this.updatePageSize}/>
                                </Tab>
                            </Tabs>
                        </Col>
                    </Row>
                </Container>
                {/*modal contents*/}
                <NewCreateDialog modaltitle="Create new Download" attributes={this.state.attributes}
                                 show={this.state.show} onClose={this.handleClose} onCreate={this.onCreate}/>
            </React.Fragment>
        )
    }
}

class NewCreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {serverList: [], channelList: [], userList:[]};

    }

    handleSubmit(e) {
        e.preventDefault();

        const newDownload = {};
        newDownload["serverId"] = ReactDOM.findDOMNode(this.refs["server"]).value.trim();
        newDownload["channelId"] = ReactDOM.findDOMNode(this.refs["channel"]).value.trim();
        newDownload["userId"] = ReactDOM.findDOMNode(this.refs["user"]).value.trim();
        newDownload["fileRefId"] = ReactDOM.findDOMNode(this.refs["fileRefId"]).value.trim();
        this.props.onCreate(newDownload);

        // this.props.attributes.forEach(attribute => {
        //     ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        // });

    }

    loadServerList() {

        axios.get('http://localhost:8080/data/servers/')
            .then((response) => {
                this.setState({
                    serverList: response.data
                });
            })
            .catch( (error) => {
                console.log(error);
            });


        // follow(client, root, [
        //     {rel: 'servers'}]
        // ).then(serverList => {
        //     return client({
        //         method: 'GET',
        //         path: serverList.entity._links.profile.href,
        //         headers: {'Accept': 'application/schema+json'}
        //     }).then(schema => {
        //         this.schema = schema.entity;
        //         return serverList;
        //     });
        // }).done(serverList => {
        //     this.setState({
        //         serverList: serverList.entity._embedded.servers,
        //     });
        // });
    }

    loadChannelList() {

        axios.get('http://localhost:8080/data/channels/')
            .then((response) => {
                this.setState({
                    channelList: response.data
                });
            })
            .catch( (error) => {
                console.log(error);
            });

        // follow(client, root, [
        //     {rel: 'channels'}]
        // ).then(channelList => {
        //     return client({
        //         method: 'GET',
        //         path: channelList.entity._links.profile.href,
        //         headers: {'Accept': 'application/schema+json'}
        //     }).then(schema => {
        //         this.schema = schema.entity;
        //         return channelList;
        //     });
        // }).done(channelList => {
        //     this.setState({
        //         channelList: channelList.entity._embedded.channels,
        //     });
        // });
    }

    loadUserList() {

        axios.get('http://localhost:8080/data/ircUsers/')
            .then((response) => {
                this.setState({
                    userList: response.data
                });
            })
            .catch( (error) => {
                console.log(error);
            });

    }

    componentDidMount() {
        this.loadServerList();
        this.loadChannelList();
        this.loadUserList();
    }

    render() {

        const serverOptions = this.state.serverList.map(server =>{

            //{"name": "Rizon","serverUrl": "irc.rizon.net","creationDate": "2019-05-29T14:56:37.599"}
            let jsonServer = JSON.stringify({id: server.id, name: server.name , serverUrl: server.serverUrl , creationDate: "2019-05-29T14:56:37.599"});
            return <option key={server.id} value={server.id}>{server.name}</option>

        });

        const userOptions = this.state.userList.map(user =>{

            // let jsonUser = JSON.stringify({id: user.id, name: user.name});
            return <option key={user.id} value={user.id}>{user.name}</option>

        });

        const channelOptions = this.state.channelList.map(channel =>{

            // let jsonChannel = JSON.stringify({id: channel.id, name: channel.name});
            return <option key={channel.id} value={channel.id}>{channel.name}</option>

        });

        const inputs = this.props.attributes.map(attribute =>{
            
            let input = "";
            switch(attribute) {
                case 'server':
                    input = 
                    <InputGroup className="mb-3" key={attribute}>
                        <InputGroup.Prepend>
                            <InputGroup.Text id="basic-addon1" key={attribute}>Server</InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control key={ attribute } ref={attribute} as="select">
                            {serverOptions}
                        </Form.Control>
                    </InputGroup>;
                    break;
                case 'channel':

                    input = 
                    <InputGroup className="mb-3" key={attribute}>
                        <InputGroup.Prepend>
                            <InputGroup.Text id="basic-addon1" key={attribute}>Channel</InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control key={ attribute } ref={attribute} as="select">
                            {channelOptions}
                        </Form.Control>
                    </InputGroup>;
                    break;
                case 'user':

                    input = 
                    <InputGroup className="mb-3" key={attribute}>
                        <InputGroup.Prepend>
                            <InputGroup.Text id="basic-addon1"  key={attribute}>User</InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control key={ attribute } ref={attribute} as="select">
                            {userOptions}
                        </Form.Control>
                    </InputGroup>;
                    break;
                case 'fileRefId':
                        input =                     
                        <InputGroup className="mb-3" key={attribute}>
                            <InputGroup.Prepend>
                                <InputGroup.Text id="basic-addon1" key={attribute}>File Reference ID</InputGroup.Text>
                            </InputGroup.Prepend>
                            <FormControl
                                placeholder={"filereference (e.g. #2421)"}
                                ref={attribute}
                                aria-label={attribute}
                            />
                        </InputGroup>;
                break;  
                default:
                };

                return input;

                }
        );


        return (
            // rebuild this with a custom modal content which gets the inputs as prop
            // 
            <Modal centered show={this.props.show} onHide={this.handleClose}>
                <Modal.Header>
                    <Modal.Title>{this.props.modaltitle}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {inputs}
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={() => this.props.onClose()}>
                        Close
                    </Button>
                    <Button variant="success" onClick={this.handleSubmit}>Create a
                        Download</Button>
                </Modal.Footer>
            </Modal>
        )

    }


}

class DownloadList extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const downloads = this.props.downloads.map(download =>
            <Download key={download.id} download={download} onDelete={this.props.onDelete}/>
        );


        return (
            <div style={{'overflowY': 'scroll', 'height': '-webkit-fill-available', 'paddingBottom': '15%'}}>
                {downloads}
            </div>
        )
    }

}

class Download extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
        this.state = {now: 45};
    }

    handleDelete() {
        this.props.onDelete(this.props.download);
    }

    render() {
        return (

            <Card style={{margin: '10px'}}>
                <Card.Header>Filename: {this.props.download.filename}</Card.Header>
                <Card.Body>
                    <Container fluid>
                        <InputGroup>
                            <ProgressBar style={{height: '30px', width: '90%'}} animated
                                         now={this.props.download.progress}
                                         label={this.props.download.status + ' ' + this.props.download.progress}/>
                            <InputGroup.Append>
                                <Button size="sm" title="Pause Download" style={{color: 'white', height: '30px'}} variant="warning">
                                    <i className="fas fa-pause"></i>
                                </Button>
                                <Button size="sm" title="Cancel Download" style={{height: '30px'}} variant="danger">
                                    <i className="fas fa-ban"></i>
                                </Button>
                            </InputGroup.Append>
                        </InputGroup>
                    </Container>
                </Card.Body>
            </Card>
        )
    }
}


ReactDOM.render(
    <App/>,
    document.getElementById('react')
)
