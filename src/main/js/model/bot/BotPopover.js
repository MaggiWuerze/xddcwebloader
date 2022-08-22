import axios from "axios";
import {Button, Card, Form, FormControl, InputGroup, OverlayTrigger, Popover} from 'react-bootstrap';


const React = require('react');
const ReactDOM = require('react-dom');

export default class BotPopover extends React.Component {

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
					console.debug(error);
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
					console.debug(error);
				});
	}

	reloadData(show) {
		console.debug(show);
		if(show){
			this.loadServerList();
			this.loadChannelList();
		}
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
									<InputGroup className="mb-3" key={attribute + "-group"}>
										<InputGroup.Text id="basic-addon1" key={attribute + "-text"}>Server</InputGroup.Text>
										<Form.Control key={attribute + "-control"} ref={attribute} as="select">
											{serverOptions}
										</Form.Control>
									</InputGroup>;
							break;
						case 'channel':
							input =
									<InputGroup className="mb-3" key={attribute + "-group"}>
										<InputGroup.Text id="basic-addon1" key={attribute + "-text"}>Channel</InputGroup.Text>
										<Form.Control key={attribute + "-control"} ref={attribute} as="select">
											{channelOptions}
										</Form.Control>
									</InputGroup>;
							break;
						case 'name':
							input =
									<InputGroup className="mb-3" key={attribute + "-group"}>
										<InputGroup.Text id="basic-addon1" key={attribute + "-text"}>{attribute}</InputGroup.Text>
										<FormControl key={attribute + "-control"}
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
										/>
									</InputGroup>;
						break;
						case 'pattern':
							input =
									<InputGroup className="mb-3" key={attribute + "-group"}>
										<InputGroup.Text id="basic-addon1" key={attribute + "-text"}>{attribute}</InputGroup.Text>
										<FormControl key={attribute + "-control"}
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
										/>
									</InputGroup>;
						break;
						case 'maxParallelDownloads':
							input =
									<InputGroup className="mb-3" key={attribute + "-group"}>
										<InputGroup.Text id="basic-addon1" key={attribute + "-text"}>{attribute}</InputGroup.Text>
										<FormControl key={attribute + "-control"}
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
										/>
									</InputGroup>;
						break;
						default:
							break;
					}
					return input;
				}
		);

		return (
				<>
					<OverlayTrigger
							trigger="click"
							key="bot-po"
							placement="top"
							rootClose
							onToggle={(show) => this.reloadData(show)}
							overlay={
								<Popover className="bot-popover" id={`bot-popover`}>
									<Popover.Header as="h3">{this.props.modaltitle}</Popover.Header>
									<Popover.Body>
										{inputs}
										<Button variant="success" onClick={(e) => this.handleSubmit(e)}>
											Create a Bot
										</Button>
									</Popover.Body>
								</Popover>
							}
					>
						<Button size="lg" className={"tab_btn"} variant="success">
							<i className="fas fa-plus"></i>
						</Button>
					</OverlayTrigger>
				</>
		)
	}


}
