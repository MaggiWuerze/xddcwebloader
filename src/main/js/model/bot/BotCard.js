import {Button, Card, Container, FormControl, InputGroup, ListGroup} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');


export default class BotCard extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleKeyDown = this.handleKeyDown.bind(this);
        this.state = {
            open: false,
        };
    }

    handleDelete() {
        this.props.onDelete(this.props.bot);
    }

    handleKeyDown(e) {

        if (e.key === 'Enter') {
            this.handleSubmit(e);
        }
    }

    handleSubmit(e) {

        e.preventDefault();
        const download = {};
        download["targetBotId"] = this.props.bot.id;
        download["fileRefId"] = ReactDOM.findDOMNode(this.refs[this.props.bot.id + "-fileRefId"]).value.trim();
        this.props.onCreate(download, "downloads", null);

        var ref = this.props.bot.id + "-fileRefId";
        ReactDOM.findDOMNode(this.refs[ref]).value = '';

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