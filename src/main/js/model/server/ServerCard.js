import {Button, Card, Container, FormControl, InputGroup, ListGroup} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');


export default class ServerCard extends React.Component {

	constructor(props) {
		super(props);
		this.handleDelete = this.handleDelete.bind(this);
	}

	handleDelete() {
		this.props.onDelete(this.props.server, 'SRV');
	}

	render() {

		return (
				<Card style={{margin: '10px'}}>
					<Card.Header>
						{this.props.server.name}
						<Button
								size="sm"
								title="Remove Server"
								style={{height: '30px', float: 'right'}}
								variant="danger"
								onClick={() => this.handleDelete()}>
							<i className="fas fa-trash"></i>
						</Button>
					</Card.Header>
					<Card.Body>
						<Container fluid>
							<ListGroup variant="flush">
								<ListGroup.Item>
									<InputGroup size="sm">
											<InputGroup.Text>Servername</InputGroup.Text>
										<FormControl
												disabled
												value={this.props.server.name}
												aria-label="file reference id"/>
									</InputGroup>
								</ListGroup.Item>
								<ListGroup.Item>
									<InputGroup size="sm">
										<InputGroup.Text>Server URL</InputGroup.Text>
										<FormControl
												disabled
												value={this.props.server.serverUrl}
												aria-label="file reference id"/>
									</InputGroup>
								</ListGroup.Item>
							</ListGroup>
						</Container>
					</Card.Body>
				</Card>
		)
	}
}