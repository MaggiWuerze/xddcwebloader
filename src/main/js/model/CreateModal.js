import axios from "axios";
import {Button, Form, FormControl, InputGroup, Modal} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');

export default class CreateModal extends React.Component {

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
					console.debug(error);
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
					console.debug(error);
				});

	}

	loadUserList() {

		axios.get('http://localhost:8080/data/ircUsers/')
				.then((response) => {
					this.setState({
						userList: response.data
					});
				})
				.catch((error) => {
					console.debug(error);
				});

	}

	componentDidMount() {
		this.loadServerList();
		this.loadChannelList();
		this.loadUserList();
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

		const userOptions = this.state.userList.map(user => {

			return <option key={user.id} value={user.id}>{user.name}</option>

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
										<Form.Control key={attribute} ref={attribute} as="select">
											{serverOptions}
										</Form.Control>
									</InputGroup>;
							break;
						case 'channel':

							input =
									<InputGroup className="mb-3" key={attribute}>
										<InputGroup.Text id="basic-addon1" key={attribute}>Channel</InputGroup.Text>
										<Form.Control key={attribute} ref={attribute} as="select">
											{channelOptions}
										</Form.Control>
									</InputGroup>;
							break;
						case 'user':

							input =
									<InputGroup className="mb-3" key={attribute}>
										<InputGroup.Text id="basic-addon1" key={attribute}>User</InputGroup.Text>
										<Form.Control key={attribute} ref={attribute} as="select">
											{userOptions}
										</Form.Control>
									</InputGroup>;
							break;
						case 'fileRefId':
							input =
									<InputGroup className="mb-3" key={attribute}>
										<InputGroup.Text id="basic-addon1" key={attribute}>File Reference ID</InputGroup.Text>
										<FormControl
												placeholder={"filereference (e.g. #2421)"}
												ref={attribute}
												aria-label={attribute}
										/>
									</InputGroup>;
							break;
						default:
							input =
									<InputGroup className="mb-3" key={attribute}>
										<InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
										<FormControl
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
										/>
									</InputGroup>;

					}


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
