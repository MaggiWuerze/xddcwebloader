import axios from "axios";
import {
    Button,
    Card,
    Form,
    FormControl,
    InputGroup,
    Popover
} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');

export default class ServerInputs extends React.Component {

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

        const newServer = {};
        newBot["name"] = ReactDOM.findDOMNode(this.refs["name"]).value.trim();
        newBot["pattern"] = ReactDOM.findDOMNode(this.refs["pattern"]).value.trim();
        newBot["serverId"] = ReactDOM.findDOMNode(this.refs["server"]).value.trim();
        newBot["channelId"] = ReactDOM.findDOMNode(this.refs["channel"]).value.trim();
        newBot["fileRefId"] = ReactDOM.findDOMNode(this.refs["fileRefId"]).value.trim();
        this.props.onCreate(newServer, 'servers', 'showServerPopover');

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

    }

    componentDidMount() {
        this.loadServerList();
        this.loadChannelList();
    }

    render() {

        const serverOptions = this.state.serverList.map(server => {

            let jsonServer = JSON.stringify({
                id: server.id,
                name: server.name,
                serverUrl: server.serverUrl,
                creationDate: "2019-05-29T14:56:37.599"
            });
            return <option key={server.id} value={server.id}>{server.name}</option>

        });

        const channelOptions = this.state.channelList.map(channel => {

            return <option key={channel.id} value={channel.id}>{channel.name}</option>

        });

        const inputs = this.props.attributes.map(attribute => {

                let input = "";
                switch (attribute) {
                    case 'name':
                        input =
                            <InputGroup className="mb-3" key={attribute}>
                                <InputGroup.Prepend>
                                    <InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
                                </InputGroup.Prepend>
                                <FormControl
                                    placeholder={attribute}
                                    ref={attribute}
                                    aria-label={attribute}
                                />
                            </InputGroup>;
                    case 'serverUrl':
                        input =
                            <InputGroup className="mb-3" key={attribute}>
                                <InputGroup.Prepend>
                                    <InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
                                </InputGroup.Prepend>
                                <FormControl
                                    placeholder={attribute}
                                    ref={attribute}
                                    aria-label={attribute}
                                />
                            </InputGroup>;
                    default:
                        break;
                }

                return input;

            }
        );


        return (
             <>
                <Card>
                  <Card.Header>{this.props.modalTitle}</Card.Header>
                  <Card.Body>
                        {inputs}
                  </Card.Body>
                  <Card.Footer className="text-muted">
                    <Button variant="success" onClick={this.handleSubmit}>
                        Create a Server
                    </Button>
                  </Card.Footer>
                </Card>

            </>
        )

    }


}
