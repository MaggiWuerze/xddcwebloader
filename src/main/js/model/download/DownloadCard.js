import {Alert, Button, Card, Container, InputGroup, ProgressBar} from "react-bootstrap";

const React = require('react');
const ReactDOM = require('react-dom');

export default class DownloadCard extends React.Component {

	constructor(props) {
		super(props);
		this.handleDelete = this.handleDelete.bind(this);
		this.handleCancel = this.handleCancel.bind(this);
		this.state = {now: 0};
	}

	handleDelete() {
		this.props.onDelete(this.props.download, "DL");
	}

	handleCancel() {
		this.props.onCancel(this.props.download, "DL");
	}

	decideAlertType() {

		switch (this.props.download.status) {

			case 'TRANSMITTING':

				return 'info';
				break;

			case 'ERROR':

				return 'danger';
				break;

			case "DONE":

				return 'success';
				break;

			default:

				return 'warning';
		}

	}

	render() {

		return (

				<Card style={{margin: '10px'}}>
					<Card.Header>
						#{this.props.download.id}
					</Card.Header>
					<Card.Body>
						<Alert show={this.props.download.status == "ERROR"} variant='danger'>
							{this.props.download.statusMessage}
						</Alert>
						<Container fluid>
							<InputGroup>
								<Card.Subtitle className="mb-2 text-muted">
									File: {this.props.download.filename} ({this.props.download.fileRefId}){"\n"}
									Size: {this.props.download.filesize}{"\n"}
								</Card.Subtitle>
								<ProgressBar style={{height: '30px', width: '90%'}}
										animated={this.props.download.status == 'TRANSMITTING'}
										variant={this.decideAlertType()}
										now={this.props.download.progress}
										label={this.props.download.status + ' (' + this.props.download.progress + '%)' + (this.props.download.status != "DONE" ? ' @ ' + this.props.download.averageSpeed + "/s" + " time remaining: " + this.props.download.timeRemaining : "")}/>
								<Button
										size="sm"
										title="Remove Download"
										style={{height: '30px'}}
										variant="danger"
										onClick={() => this.handleDelete()}>
									<i className="fas fa-trash"></i>
								</Button>
							</InputGroup>
						</Container>
					</Card.Body>
				</Card>
		)
	}

}