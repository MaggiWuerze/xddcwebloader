'use strict';

import {
    Alert,
    Button,
    Card,
    Col,
    Container,
    Form,
    FormControl,
    InputGroup,
    ListGroup,
    Modal,
    Navbar,
    Nav,
    ProgressBar,
    Row,
    Tab,
    Tabs, TabContent
} from 'react-bootstrap';
import axios from 'axios';

const React = require('react');
const ReactDOM = require('react-dom');
// const client = require('./client');
// const follow = require('./follow'); // function to hop multiple links by "rel"
const stompClient = require('./websocket-listener');

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            onboarding: true,
            bots: [],
            servers: [],
            channels: [],
            downloads: [],
            doneDownloads: [],
            failedDownloads: [],
            botAttributes: [],
            channelAttributes: [],
            serverAttributes: [],
            links: {}
        };
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.handleSocketCall = this.handleSocketCall.bind(this);
        this.toggleBoolean = this.toggleBoolean.bind(this);
    }

    loadFromServer() {

        axios
            .get('http://localhost:8080/data/initialized/', {

                params: {
                    active: true,
                }

            })
            .then((response) => {

                var init = response.data[0];
                this.setState({
                    onboarding: init
                });

            })
            .catch((error) => {
                console.log(error);
            });

        axios
            .get('http://localhost:8080/data/bots/', {

                params: {
                    active: true,
                }

            })
            .then((response) => {

                response.data[0] ? this.updateAttributes(Object.keys(response.data[0])) : null;
                this.setState({
                    bots: response.data
                });

            })
            .catch((error) => {
                console.log(error);
            });

        axios
            .get('http://localhost:8080/data/downloads/active/', {

                params: {
                    active: true,
                }

            })
            .then((response) => {

                this.setState({
                    downloads: response.data
                });

            })
            .catch((error) => {
                console.log(error);
            });

        axios
            .get('http://localhost:8080/data/downloads/failed')
            .then((response) => {

                this.setState({
                    failedDownloads: response.data
                });

            })
            .catch((error) => {
                console.log(error);
            });

        axios
            .get('http://localhost:8080/data/downloads/active/', {

                params: {
                    active: false,
                }

            })
            .then((response) => {

                this.setState({
                    doneDownloads: response.data
                });

            })
            .catch((error) => {
                console.log(error);
            });

    }

    onCreate(object, objectName, modalName) {

        axios
            .post('http://localhost:8080/data/' + objectName + '/', object)
            .then((response) => {

                if (response.status.toString() != '200') {

                    alert("there was an error!");

                } else if (modalName) {

                    this.toggleBoolean(modalName);
                }
            });

    }

    onDelete(payload, type) {
        // client({method: 'DELETE', path: payload._links.self.href}).done(response => {
        //     this.loadFromServer(this.state.pageSize);
        // });
    }

    moveToList(sourceList, targetList, item) {

        this.removeFromListById(sourceList, item.id);
        return this.addToListAndSort(targetList, item);

    }

    addToListAndSort(list, newItem) {

        // this.setState(state => {
        const downloads = list;
        downloads.push(newItem);
        downloads.sort(function (a, b) {
            // return (a.progress < b.progress) ? -1 : (a.progress > b.progress) ? 1 : 0;
            //multidimension sort?
            return a.progress - b.progress || a.date - b.date;
        });

        return downloads;
        //     return {downloads,};
        // });

    }

    removeFromListById(targetList, idToRemove) {

        var list = targetList.filter(item => item.id !== idToRemove);
        return list;

        // this.setState(state => {
        //     const list = targetList.filter(item => item.id !== idToRemove);
        //     return {list,};
        // });

    }

    updateItemAndSort(list, newItem) {

        this.setState(state => {
            const downloads = list.map((item) => {
                if (item.id == newItem.id) {
                    return newItem;
                } else {
                    return item;
                }
            });

            downloads.sort(function (a, b) {
                //multidimension sort?
                return (a.progress > b.progress) ? -1 : (a.progress < b.progress) ? 1 : 0;
                // return a.progress - b.progress || a.date - b.date;
            });
            return {downloads,};
        });

    }

    updateAttributes(attributes) {

        if (this.state.botAttributes && this.state.botAttributes.length >= 0) {
            this.setState({
                botAttributes: attributes
            });
        }
    }

    handleSocketCall(responseObj) {

        let message = JSON.parse(responseObj.body);
        let downloads = this.state.downloads;
        let failedDownloads = this.state.failedDownloads;
        let doneDownloads = this.state.doneDownloads;

        switch (responseObj.headers.destination) {

            case '/topic/newDownload':

                this.setState({

                    downloads: this.addToListAndSort(downloads, message)

                });

                break;

            case '/topic/updateDownload':

                if (message.status == 'DONE') {

                    this.setState({

                        downloads: this.removeFromListById(downloads, message.id),
                        doneDownloads: this.addToListAndSort(doneDownloads, message)

                    });
                    // this.moveToList(downloads, doneDownloads, message);

                } else if (message.status == 'ERROR') {

                    this.setState({

                        downloads: this.removeFromListById(downloads, message.id),
                        failedDownloads: this.addToListAndSort(failedDownloads, message)

                    });

                } else {

                    this.updateItemAndSort(downloads, message);

                }

                break;

            case '/topic/deleteDownload':

                this.removeFromListById(downloads, message.id)
                break;

            default:

                console.log("unknown event route!")
        }


    }

    initComplete(){

        axios
            .post('http://localhost:8080/data/initialized/', true)
            .then((response) => {

                if (response.status.toString() != '200') {

                    alert("there was an error!");

                } else if (modalName) {

                    this.toggleBoolean(modalName);
                }
            });

    }

    componentDidMount() {
        this.loadFromServer();
        stompClient.register([
            {route: '/topic/newDownload', callback: this.handleSocketCall},
            {route: '/topic/updateDownload', callback: this.handleSocketCall},
            {route: '/topic/deleteDownload', callback: this.handleSocketCall}
        ]);
    }

    toggleBoolean(modalname) {

        this.setState({[modalname]: !this.state[modalname]});
    }

    render() {

        if (this.state.onboarding) {

            return (
                <div>
                    "hello world!"
                    <Button size="sm" onClick={() => {this.setState({onboarding: false})}} variant="success">
                        <i className="fas fa-thumbs-up"></i>
                    </Button>
                </div>
            )

        } else {

            return (
                <React.Fragment>
                    <Navbar expand="lg">
                        <Navbar.Brand href="#home">XDCC Loader</Navbar.Brand>
                        <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                        <Navbar.Collapse id="basic-navbar-nav">
                            <Nav className="mr-auto">
                                {/*<Nav.Link onClick={() => this.toggleBoolean('showBotModal')}>Bot</Nav.Link>*/}
                                {/*<Nav.Link onClick={() => this.toggleBoolean('shoKxx0P456/wServerModal')}>Server</Nav.Link>*/}
                                {/*<Nav.Link onClick={() => this.toggleBoolean('showChannelModal')}>Channel</Nav.Link>*/}
                            </Nav>
                        </Navbar.Collapse>
                    </Navbar>
                    <Container fluid>
                        <Row>
                            <Col md={4} className={"column"}>
                                <Card className={"customCard"}>
                                    <Tab.Container defaultActiveKey="bots">
                                        <Card.Header>
                                            <Nav fill variant="tabs">
                                                <Nav.Item>
                                                    <Nav.Link eventKey="bots">
                                                    <span>
                                                        {"Bots (" + this.state.bots.length + ")"}&nbsp;
                                                        <Button size="sm" className={"tab_btn"} variant="success">
                                                            <i className="fas fa-plus"></i>
                                                        </Button>
                                                    </span>
                                                    </Nav.Link>
                                                </Nav.Item>
                                                <Nav.Item>
                                                    <Nav.Link eventKey="servers">
                                                    <span>
                                                        {"Servers (" + this.state.servers.length + ")"}&nbsp;
                                                        <Button size="sm" className={"tab_btn"} variant="success">
                                                            <i className="fas fa-plus"></i>
                                                        </Button>
                                                    </span>
                                                    </Nav.Link>
                                                </Nav.Item>
                                                <Nav.Item>
                                                    <Nav.Link eventKey="channels">
                                                    <span>
                                                        {"Channels (" + this.state.channels.length + ")"}&nbsp;
                                                        <Button size="sm" className={"tab_btn"} variant="success">
                                                            <i className="fas fa-plus"></i>
                                                        </Button>
                                                    </span>
                                                    </Nav.Link>
                                                </Nav.Item>
                                            </Nav>
                                        </Card.Header>
                                        <Card.Body>
                                            <TabContent>
                                                <Tab.Pane eventKey="bots">
                                                    <BotList bots={this.state.bots} onDelete={this.onDelete()}
                                                             onCreate={this.onCreate}/>
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="server">
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="channels">
                                                </Tab.Pane>
                                            </TabContent>
                                        </Card.Body>
                                    </Tab.Container>
                                </Card>

                            </Col>
                            <Col md={8} className={"column"}>
                                <Card className={"customCard"}>
                                    <Tab.Container defaultActiveKey="activeDownloads">
                                        <Card.Header>
                                            <Nav fill variant="tabs">
                                                <Nav.Item>
                                                    <Nav.Link
                                                        eventKey="activeDownloads">{"Active Downloads (" + this.state.downloads.length + ")"}</Nav.Link>
                                                </Nav.Item>
                                                <Nav.Item>
                                                    <Nav.Link
                                                        eventKey="completedDownloads">{"Completed (" + this.state.doneDownloads.length + ")"}</Nav.Link>
                                                </Nav.Item>
                                                <Nav.Item>
                                                    <Nav.Link
                                                        eventKey="failedDownloads">{"Failed (" + this.state.failedDownloads.length + ")"}</Nav.Link>
                                                </Nav.Item>
                                            </Nav>
                                        </Card.Header>
                                        <Card.Body>
                                            <TabContent>
                                                <Tab.Pane eventKey="activeDownloads">
                                                    <DownloadList downloads={this.state.downloads}
                                                                  onDelete={this.onDelete}/>
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="completedDownloads">
                                                    <DownloadList downloads={this.state.doneDownloads}
                                                                  onDelete={this.onDelete}/>
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="failedDownloads">
                                                    <DownloadList downloads={this.state.failedDownloads}
                                                                  onDelete={this.onDelete}/>
                                                </Tab.Pane>
                                            </TabContent>
                                        </Card.Body>
                                    </Tab.Container>
                                </Card>
                            </Col>
                        </Row>
                    </Container>
                    {/*modal contents*/}
                    <CreateBotModal modaltitle="Create new Download" botAttributes={this.state.botAttributes}
                                    show={this.state.showBotModal} onClose={() => this.toggleBoolean('showBotModal')}
                                    onCreate={this.onCreate}/>
                </React.Fragment>
            )
        }
    }
}

class CreateBotModal extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {serverList: [], channelList: [], userList: []};

    }

    handleSubmit(e) {
        e.preventDefault();

        // this.name = name;
        // this.pattern = pattern;
        // this.serverId = serverId;
        // this.channelId = channelId;
        // this.fileRefId = fileRefId;

        const newBot = {};
        newBot["name"] = ReactDOM.findDOMNode(this.refs["name"]).value.trim();
        newBot["pattern"] = ReactDOM.findDOMNode(this.refs["pattern"]).value.trim();
        newBot["serverId"] = ReactDOM.findDOMNode(this.refs["server"]).value.trim();
        newBot["channelId"] = ReactDOM.findDOMNode(this.refs["channel"]).value.trim();
        newBot["fileRefId"] = ReactDOM.findDOMNode(this.refs["fileRefId"]).value.trim();
        this.props.onCreate(newBot, 'bot', 'showBotModal');

        // this.props.botAttributes.forEach(attribute => {
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
            .catch((error) => {
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
            .catch((error) => {
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
            .catch((error) => {
                console.log(error);
            });

    }

    componentDidMount() {
        this.loadServerList();
        this.loadChannelList();
        this.loadUserList();
    }

    render() {

        const serverOptions = this.state.serverList.map(server => {

            //{"name": "Rizon","serverUrl": "irc.rizon.net","creationDate": "2019-05-29T14:56:37.599"}
            let jsonServer = JSON.stringify({
                id: server.id,
                name: server.name,
                serverUrl: server.serverUrl,
                creationDate: "2019-05-29T14:56:37.599"
            });
            return <option key={server.id} value={server.id}>{server.name}</option>

        });

        const userOptions = this.state.userList.map(user => {

            // let jsonUser = JSON.stringify({id: user.id, name: user.name});
            return <option key={user.id} value={user.id}>{user.name}</option>

        });

        const channelOptions = this.state.channelList.map(channel => {

            // let jsonChannel = JSON.stringify({id: channel.id, name: channel.name});
            return <option key={channel.id} value={channel.id}>{channel.name}</option>

        });

        const inputs = this.props.botAttributes.map(attribute => {

                let input = "";
                switch (attribute) {
                    case 'server':
                        input =
                            <InputGroup className="mb-3" key={attribute}>
                                <InputGroup.Prepend>
                                    <InputGroup.Text id="basic-addon1" key={attribute}>Server</InputGroup.Text>
                                </InputGroup.Prepend>
                                <Form.Control key={attribute} ref={attribute} as="select">
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
                                <Form.Control key={attribute} ref={attribute} as="select">
                                    {channelOptions}
                                </Form.Control>
                            </InputGroup>;
                        break;
                    case 'user':

                        input =
                            <InputGroup className="mb-3" key={attribute}>
                                <InputGroup.Prepend>
                                    <InputGroup.Text id="basic-addon1" key={attribute}>User</InputGroup.Text>
                                </InputGroup.Prepend>
                                <Form.Control key={attribute} ref={attribute} as="select">
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
                }
                ;

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
            <div className={'list'}>
                {downloads}
            </div>
        )
    }

}

class BotList extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const bots = this.props.bots.map(bot =>
            <Bot key={bot.id} bot={bot} onDelete={this.props.onDelete} onCreate={this.props.onCreate}/>
        );


        return (
            <div style={{'overflowY': 'auto', 'height': '-webkit-fill-available', 'paddingBottom': '15%'}}>
                {bots}
            </div>
        )
    }

}

class Download extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
        this.state = {now: 0};
    }

    handleDelete() {
        this.props.onDelete(this.props.download);
    }

    render() {
        return (

            <Card style={{margin: '10px'}}>
                <Card.Header>({this.props.download.fileRefId}) Filename: {this.props.download.filename}</Card.Header>
                <Card.Body>
                    <Alert show={this.props.download.status == "ERROR"} variant='danger'>
                        {this.props.download.statusMessage}
                    </Alert>
                    <Container fluid>
                        <InputGroup>
                            <ProgressBar style={{height: '30px', width: '90%'}} animated={this.props.download.status == 'TRANSMITTING'}
                                         now={this.props.download.progress}
                                         label={this.props.download.status + ' (' + this.props.download.progress + '%)'}/>
                            <InputGroup.Append>
                                <Button size="sm" title="Pause Download" style={{color: 'white', height: '30px'}}
                                        variant="warning">
                                    <i className="fas fa-stop"></i>
                                </Button>
                                <Button size="sm" title="Cancel Download" style={{height: '30px'}} variant="danger">
                                    <i className="fas fa-trash"></i>
                                </Button>
                            </InputGroup.Append>
                        </InputGroup>
                    </Container>
                </Card.Body>
            </Card>
        )
    }
}

class Bot extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {
            open: false,
        };
    }

    handleDelete() {
        this.props.onDelete(this.props.bot);
    }

    handleKeyDown(e) {
        if (e.key === 'Enter') {
            console.log(e);
            var ref = this.props.bot.id + "-fileRefId";
            console.log(ref);
            var input = ReactDOM.findDOMNode(this.refs[ref]);
            input.value = '';
            this.handleSubmit();
        }
    }

    handleSubmit(e) {
        e.preventDefault();

        const newBot = {};
        newBot["targetBotId"] = this.props.bot.id;
        newBot["fileRefId"] = ReactDOM.findDOMNode(this.refs[this.props.bot.id + "-fileRefId"]).value.trim();
        this.props.onCreate(newBot, "downloads", null);

        // this.props.botAttributes.forEach(attribute => {
        //     ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        // });

    }

    render() {

        const open = this.state.open;
        return (
            <Card style={{margin: '10px'}}>
                <Card.Header onClick={() => this.setState({open: !open})}>{this.props.bot.name}</Card.Header>
                <Card.Body>
                    {/*<Collapse in={this.state.open}>*/}
                    <Container fluid>
                        <ListGroup variant="flush">
                            <ListGroup.Item>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <InputGroup.Text>Channelname</InputGroup.Text>
                                    </InputGroup.Prepend>
                                    <FormControl
                                        disabled
                                        value={this.props.bot.channel.name}
                                        aria-label="file reference id"
                                    />
                                </InputGroup>
                                {/*{"Channelname: " + this.props.bot.channel.name}*/}
                            </ListGroup.Item>
                            <ListGroup.Item>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <InputGroup.Text>Servername</InputGroup.Text>
                                    </InputGroup.Prepend>
                                    <FormControl
                                        disabled
                                        value={this.props.bot.server.name}
                                        aria-label="file reference id"
                                    />
                                </InputGroup>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <InputGroup.Text>Bot Messagepattern</InputGroup.Text>
                                    </InputGroup.Prepend>
                                    <FormControl
                                        disabled
                                        value={this.props.bot.pattern}
                                        aria-label="file reference id"
                                    />
                                </InputGroup>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                <InputGroup size="sm">
                                    <FormControl
                                        onKeyDown={this.handleKeyDown}
                                        ref={this.props.bot.id + "-fileRefId"}
                                        placeholder="fileRefId (eg. #3452)"
                                        aria-label="file reference id"
                                    />
                                    <InputGroup.Append>
                                        <Button variant="outline-secondary" onClick={this.handleSubmit}>Send</Button>
                                    </InputGroup.Append>
                                </InputGroup>
                            </ListGroup.Item>
                        </ListGroup>
                    </Container>
                    {/*</Collapse>*/}
                </Card.Body>
            </Card>
        )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)
