import {Button, Card, FormControl, InputGroup, OverlayTrigger, Popover} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');

export default class ChannelPopover extends React.Component {

	constructor(props) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.state = {serverList: [], channelList: [], userList: []};

	}

	handleSubmit(e) {
		e.preventDefault();

		const newChannel = {};
		newChannel["name"] = ReactDOM.findDOMNode(this.refs["name"]).value.trim();
		this.props.onCreate(newChannel, 'channels', 'showChannelPopover', this.props.onFinish);
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
							key="channel-po"
							placement="top"
							rootClose
							overlay={
								<Popover id={`channel-popover`}>
									<Popover.Header as="h3">{this.props.modaltitle}</Popover.Header>
									<Popover.Body>
										{inputs}
										<Button variant="success" onClick={(e) => this.handleSubmit(e)}>
											Create a Channel
										</Button>
									</Popover.Body>
								</Popover>
							}
					>
						<Button size="lg" className={"tab_btn"} variant="success" onClick={() => this.setState({showChannelPopover: !this.state.showChannelPopover})}>
							<i className="fas fa-plus"></i>
						</Button>
					</OverlayTrigger>
				</>

		)
	}
}
