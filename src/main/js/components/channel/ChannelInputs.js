import {Button, Card, FormControl, InputGroup} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');

export default class ChannelInputs extends React.Component {

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
								Create a Channel
							</Button>
						</Card.Footer>
					</Card>
				</>
		)
	}
}
