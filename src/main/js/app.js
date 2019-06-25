'use strict';

import BotListView from './model/bot/BotListView';
import DownloadListView from './model/download/DownloadListView';
import ServerListView from './model/server/ServerListView';
import ChannelListView from './model/channel/ChannelListView';
import CreateModal from './model/CreateModal';

import {
    Button,
    Card,
    Col,
    Container,
    Navbar,
    Nav,
    Row,
    Tab,
    TabContent
} from 'react-bootstrap';
import axios from 'axios';

const React = require('react');
const ReactDOM = require('react-dom');
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

                response.data[0] ? this.updateAttributes(Object.keys(response.data[0]), 'botAttributes') : null;
                this.setState({
                    bots: response.data
                });

            })
            .catch((error) => {
                console.log(error);
            });
        axios
            .get('http://localhost:8080/data/servers/')
            .then((response) => {

                response.data[0] ? this.updateAttributes(Object.keys(response.data[0]), 'serverAttributes') : null;
                this.setState({
                    servers: response.data
                });

            })
            .catch((error) => {
                console.log(error);
            });
        axios
            .get('http://localhost:8080/data/channels/')
            .then((response) => {

                response.data[0] ? this.updateAttributes(Object.keys(response.data[0]), 'channelAttributes') : null;
                this.setState({
                    channels: response.data
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

    updateAttributes(attributes, paramName) {

        if (this.state[paramName] && this.state[paramName].length >= 0) {
            this.setState({[paramName]: attributes});
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

    initComplete() {

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
                    <Button size="sm" onClick={() => {
                        this.setState({onboarding: false})
                    }} variant="success">
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
                                                        <Button size="sm" className={"tab_btn"} variant="success"
                                                                onClick={() => this.toggleBoolean('showBotModal')}>
                                                            <i className="fas fa-plus"></i>
                                                        </Button>
                                                    </span>
                                                    </Nav.Link>
                                                </Nav.Item>
                                                <Nav.Item>
                                                    <Nav.Link eventKey="servers">
                                                    <span>
                                                        {"Servers (" + this.state.servers.length + ")"}&nbsp;
                                                        <Button size="sm" className={"tab_btn"} variant="success"
                                                                onClick={() => this.toggleBoolean('showServerModal')}>
                                                            <i className="fas fa-plus"></i>
                                                        </Button>
                                                    </span>
                                                    </Nav.Link>
                                                </Nav.Item>
                                                <Nav.Item>
                                                    <Nav.Link eventKey="channels">
                                                    <span>
                                                        {"Channels (" + this.state.channels.length + ")"}&nbsp;
                                                        <Button size="sm" className={"tab_btn"} variant="success"
                                                                onClick={() => this.toggleBoolean('showChannelModal')}>
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
                                                    <BotListView bots={this.state.bots} onDelete={this.onDelete()}
                                                             onCreate={this.onCreate}/>
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="servers">
                                                    <ServerListView servers={this.state.servers} onDelete={this.onDelete()}
                                                             onCreate={this.onCreate}/>
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="channels">
                                                    <ChannelListView channels={this.state.channels} onDelete={this.onDelete()}
                                                             onCreate={this.onCreate}/>
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
                                                    <DownloadListView downloads={this.state.downloads}
                                                                  onDelete={this.onDelete}/>
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="completedDownloads">
                                                    <DownloadListView downloads={this.state.doneDownloads}
                                                                  onDelete={this.onDelete}/>
                                                </Tab.Pane>
                                                <Tab.Pane eventKey="failedDownloads">
                                                    <DownloadListView downloads={this.state.failedDownloads}
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
                    <CreateModal modaltitle="Create new Bot" attributes={this.state.botAttributes}
                                    show={this.state.showBotModal} onClose={() => this.toggleBoolean('showBotModal')}
                                    onCreate={this.onCreate}/>
                    <CreateModal modaltitle="Create new Server" attributes={this.state.serverAttributes}
                                    show={this.state.showServerModal} onClose={() => this.toggleBoolean('showServerModal')}
                                    onCreate={this.onCreate}/>
                    <CreateModal modaltitle="Create new Channel" attributes={this.state.channelAttributes}
                                    show={this.state.showChannelModal} onClose={() => this.toggleBoolean('showChannelModal')}
                                    onCreate={this.onCreate}/>
                </React.Fragment>
            )
        }
    }
}


ReactDOM.render(
    <App/>,
    document.getElementById('react')
)
