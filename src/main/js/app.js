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

    // tag::follow-2[]
    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'downloads', params: {size: pageSize}}]
        ).then(downloadCollection => {
            return client({
                method: 'GET',
                path: downloadCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                return downloadCollection;
            });
        }).done(downloadCollection => {
            this.setState({
                downloads: downloadCollection.entity._embedded.downloads,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: downloadCollection.entity._links
            });
        });
    }

    // end::follow-2[]

    // tag::create[]
    onCreate(newUser) {
        follow(client, root, ['downloads']).then(downloadCollection => {
            return client({
                method: 'POST',
                path: downloadCollection.entity._links.self.href,
                entity: newUser,
                headers: {'Content-Type': 'application/json'}
            })
        }).then(response => {
            return follow(client, root, [
                {rel: 'downloads', params: {'size': 99}}]);
        }).done(response => {

            alert(response);

            // if (typeof response.entity._links.last !== "undefined") {
            //     this.onNavigate(response.entity._links.last.href);
            // } else {
            //     this.onNavigate(response.entity._links.self.href);
            // }
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

    handleSocketCall(type){

        console.log(type);

    }

    // tag::follow-1[]
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        stompClient.register([
            {route: '/topic/newDownload', callback: this.handleSocketCall("new")},
            {route: '/topic/updateDownload', callback: this.handleSocketCall("update")},
            {route: '/topic/deleteDownload', callback: this.handleSocketCall("delete")}
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
        console.log(this.state.attributes);

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
                                </Tab>
                                <Tab eventKey="failedDownloads" title="Failed">
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
        console.log(props.modaltitle);

    }

    handleSubmit(e) {
        e.preventDefault();
        const newUser = {};
        this.props.attributes.forEach(attribute => {
            newUser[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newUser);

        // clear out the dialog's inputs
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>

            <InputGroup className="mb-3" key={attribute}>
                <InputGroup.Prepend>
                    <InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
                </InputGroup.Prepend>
                <FormControl
                    placeholder={attribute}
                    ref={attribute}
                    aria-label={attribute}
                />
            </InputGroup>
        );

        return (
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
            <Download key={download._links.self.href} download={download} onDelete={this.props.onDelete}/>
        );


        return (
            <div style={{'overflow': 'scroll', 'height': '-webkit-fill-available', 'paddingBottom': '15%'}}>
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
                <Card.Header>{this.props.download.filename}</Card.Header>
                <Card.Body>
                    <Container fluid>
                        <InputGroup>
                            <ProgressBar style={{height: '30px', width: '90%'}} animated
                                         now={this.props.download.progress}
                                         label={this.props.download.status + ' ' + this.props.download.progress}/>
                            <InputGroup.Append>
                                <Button size="sm" style={{color: 'white', height: '30px'}} variant="warning">
                                    <i className="fas fa-pause"></i>
                                </Button>
                                <Button size="sm" style={{height: '30px'}} variant="danger">
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
