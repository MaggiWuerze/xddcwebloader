import {Button, Card, FormControl, InputGroup} from "react-bootstrap";

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
		console.log("saving new server");

		const newServer = {};
		newServer["name"] = ReactDOM.findDOMNode(this.refs["name"]).value.trim();
		newServer["serverUrl"] = ReactDOM.findDOMNode(this.refs["serverUrl"]).value.trim();
		this.props.onCreate(newServer, 'servers', 'showServerPopover', this.props.onFinish);
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
							<Button variant="success" onClick={(e) => this.handleSubmit(e)}>
								Create a Server
							</Button>
						</Card.Footer>
					</Card>

				</>
		)
	}
}
