import axios from "axios";
import {Button, Card, Form, FormControl, InputGroup} from 'react-bootstrap';

const React = require('react');
const ReactDOM = require('react-dom');

export default class BotInputs extends React.Component {

	constructor(props) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.state = {serverList: [], channelList: [], userList: []};
	}

	handleSubmit(e) {
		e.preventDefault();

		const newBot = {};
		newBot["name"] = ReactDOM.findDOMNode(this.refs["name"]).value.trim();
		newBot["pattern"] = ReactDOM.findDOMNode(this.refs["pattern"]).value.trim();
		newBot["serverId"] = ReactDOM.findDOMNode(this.refs["server"]).value.trim();
		newBot["channelId"] = ReactDOM.findDOMNode(this.refs["channel"]).value.trim();
		newBot["maxParallelDownloads"] = ReactDOM.findDOMNode(this.refs["maxParallelDownloads"]).value.trim();
		this.props.onCreate(newBot, 'bots', 'showBotPopover', this.props.onFinish);
	}

	loadServerList() {
		axios.get('http://localhost:8080/servers/')
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
		axios.get('http://localhost:8080/channels/')
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
			return <option key={server.id} value={server.id}>{server.name}</option>
		});

		const channelOptions = this.state.channelList.map(channel => {
			return <option key={channel.id} value={channel.id}>{channel.name}</option>
		});

		const inputs = this.props.attributes.map(attribute => {
					let input = "";
					switch (attribute) {
						case 'server':
							input =
									<InputGroup className="mb-3" key={attribute}>

											<InputGroup.Text id="basic-addon1" key={attribute}>Server</InputGroup.Text>
										"
										<Form.Control key={attribute} ref={attribute} as="select">
											{serverOptions}
										</Form.Control>
									</InputGroup>;
							break;
						case 'channel':
							input =
									<InputGroup className="mb-3" key={attribute}>

											<InputGroup.Text id="basic-addon1" key={attribute}>Channel</InputGroup.Text>
										"
										<Form.Control key={attribute} ref={attribute} as="select">
											{channelOptions}
										</Form.Control>
									</InputGroup>;
							break;
						case 'name':
							input =
									<InputGroup className="mb-3" key={attribute}>

											<InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
										"
										<FormControl
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
										/>
									</InputGroup>;
						case 'pattern':
							input =
									<InputGroup className="mb-3" key={attribute}>

											<InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
										"
										<FormControl
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
										/>
									</InputGroup>;
						case 'maxParallelDownloads':
							input =
									<InputGroup className="mb-3" key={attribute}>

											<InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
										"
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
								Create a Bot
							</Button>
						</Card.Footer>
					</Card>

				</>
		)
	}
}
