import {Button, Card, Container, FormControl, InputGroup, ListGroup} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');


export default class Toaster extends React.Component {

	constructor(props) {
		super(props);
		this.handleDelete = this.handleDelete.bind(this);
	}

	handleDelete() {
		this.props.onDelete(this.props.channel, 'CH');
	}

	render() {

		return (
				<Card style={{margin: '10px'}}>
					<Card.Header>
						{this.props.channel.name}
						<Button
								size="sm"
								title="Remove Channel"
								style={{height: '30px', float: 'right'}}
								variant="danger"
								onClick={() => this.handleDelete()}>
							<i className="fas fa-trash"></i>
						</Button></Card.Header>
					<Card.Body>
						<Container fluid>
							<ListGroup variant="flush">
								<ListGroup.Item>
									<InputGroup size="sm">
										<InputGroup.Text>Channel Name</InputGroup.Text>
										<FormControl
												disabled
												value={this.props.channel.name}
												aria-label="file reference id"
										/>
									</InputGroup>
								</ListGroup.Item>
							</ListGroup>
						</Container>
					</Card.Body>
				</Card>
		);
	}
}