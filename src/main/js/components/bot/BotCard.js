import {Button, Card, Container, Form, InputGroup, ListGroup} from "react-bootstrap";

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
						<Container fluid>
							<ListGroup variant="flush">
								<ListGroup.Item>
									<InputGroup size="sm">
										<InputGroup.Text>Channelname</InputGroup.Text>
										<Form.Control
												disabled
												value={this.props.bot.channel.name}
												aria-label="file reference id"
										/>
									</InputGroup>
								</ListGroup.Item>
								<ListGroup.Item>
									<InputGroup size="sm">
										<InputGroup.Text>Servername</InputGroup.Text>
										<Form.Control
												disabled
												value={this.props.bot.server.name}
												aria-label="file reference id"
										/>
									</InputGroup>
								</ListGroup.Item>
								<ListGroup.Item>
									<InputGroup size="sm">
										<InputGroup.Text>Bot Messagepattern</InputGroup.Text>
										<Form.Control
												disabled
												value={this.props.bot.pattern}
												aria-label="file reference id"
										/>
									</InputGroup>
								</ListGroup.Item>
								<ListGroup.Item>
									<InputGroup size="sm">
										<InputGroup.Text>Max Parallel Downloads</InputGroup.Text>
										<Form.Control
												disabled
												value={this.props.bot.maxParallelDownloads}
												aria-label="file reference id"
										/>
									</InputGroup>
								</ListGroup.Item>
								<ListGroup.Item>
									<InputGroup size="sm">
										<Form.Control
												onKeyDown={this.handleKeyDown}
												ref={this.props.bot.id + "-fileRefId"}
												placeholder="fileRefId (eg. #3452)"
												aria-label="file reference id"
										/>
										<Button variant="outline-secondary" onClick={this.handleSubmit}>Send</Button>
									</InputGroup>
								</ListGroup.Item>
							</ListGroup>
						</Container>
					</Card.Body>
				</Card>
		)
	}
}