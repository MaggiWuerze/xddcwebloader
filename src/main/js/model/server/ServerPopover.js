import {Button, Card, FormControl, InputGroup, OverlayTrigger, Popover} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');

export default class ServerPopover extends React.Component {

	constructor(props) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.state = {serverList: [], channelList: [], userList: []};
	}

	handleSubmit(e) {
		e.preventDefault();
		console.debug("saving new server");

		const newServer = {};
		newServer["name"] = ReactDOM.findDOMNode(this.refs["name"]).value.trim();
		newServer["serverUrl"] = ReactDOM.findDOMNode(this.refs["serverUrl"]).value.trim();
		this.props.onCreate(newServer, 'servers', 'showServerPopover', this.props.onFinish);
	}

	handleInput(e) {
		// e.target.value = e.target.value + e.key;
		return false;
	}

	render() {
		const inputs = this.props.attributes.map(attribute => {
					let input = "";
					switch (attribute) {
						case 'name':
							input =
									<InputGroup className="mb-3" key={attribute}>
										<InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
										<FormControl
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
												onKeyDown={(e) => {this.handleInput(e)}}
										/>
									</InputGroup>;
						case 'serverUrl':
							input =
									<InputGroup className="mb-3" key={attribute}>
										<InputGroup.Text id="basic-addon1" key={attribute}>{attribute}</InputGroup.Text>
										<FormControl
												placeholder={attribute}
												ref={attribute}
												aria-label={attribute}
												onKeyDown={(e) => {this.handleInput(e)}}
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
					<OverlayTrigger
							trigger="click"
							key="server-po"
							placement="top"
							rootClose
							overlay={
								<Popover id={`server-popover`}>
									<Popover.Header as="h3">{this.props.modaltitle}</Popover.Header>
									<Popover.Body>
										{inputs}
										<Button variant="success" onClick={(e) => this.handleSubmit(e)}>
											Create a Server
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
