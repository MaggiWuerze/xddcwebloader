import React from 'react';
import {Button, Card, Col, ListGroup, Form, InputGroup, FormControl, DropdownButton, Dropdown} from 'react-bootstrap';
import {FileSelector} from "./FileSelector";

const ReactDOM = require('react-dom');

export default class Settings extends React.Component {


	constructor(props) {
		super(props);

		// method refs
		this.handleSave = this.handleSave.bind(this);
		this.handleReset = this.handleReset.bind(this);
		this.settingsChanged = this.settingsChanged.bind(this);
		this.handleChange = this.handleChange.bind(this);
		this.getInputs = this.getInputs.bind(this);

		//field refs
		// this.settingsForm = React.createRef();
		// this.resetBtn = React.createRef();
		// this.saveBtn = React.createRef();

		this.state = {
			settings: this.props.userSettings,
			settingsChanged: false
		};


	}

	handleChange(event) {
		if (event.target.id) {
			let value = event.target.value;
			let updatedSettings = this.state.settings;
			updatedSettings[event.target.id] = value;
			this.setState({settings: updatedSettings});
			this.setState({
				settingsChanged: true
			});
		}
	}

	settingsChanged() {
		return this.state.settingsChanged;
	}

	handleSave(e) {
		if (this.settingsChanged()) {
			console.debug(this.state.settings);
			this.props.onCreate(this.state.settings, "usersettings", "");
		}else{
			console.debug("no changes detected")
		}
	}

	handleReset(e) {
		console.debug("resetting settings");
		if (this.settingsChanged()) {
			var fileRef = document.getElementById('wizardFormFileRef');
			console.debug("with value " + fileRef);

			var download = {
				targetBotId: 1,
				fileRefId: fileRef.value
			}
			this.props.onCreate(download, "downloads", "", this.props.onFinish);

		} else {

			console.debug("without setup");
			this.props.onFinish();
		}
	}

	handleFolderSelection(e) {
		console.debug(e);
		console.debug("Directory selected!");
	}

	getInputs() {
		var settings = this.state.settings;
		var inputs = [];
		for (var property in settings) {
			var newInput = null;

			switch (property) {
				case 'refreshrateInSeconds':
					newInput =
							<ListGroup.Item key={property}>
								<InputGroup className="mb-3">
									<InputGroup.Text>{property}</InputGroup.Text>
									<FormControl
											aria-label="Default"
											aria-describedby="inputGroup-sizing-default"
											onChange={(e) => this.handleFolderSelection(e)}
											id={property}
											value={settings[property]}
									/>
								</InputGroup>
							</ListGroup.Item>
					break;

				case 'sessionTimeout':
					newInput =
							<ListGroup.Item key={property}>
								<InputGroup className="mb-3">
									<InputGroup.Text>{property}</InputGroup.Text>
									<FormControl
											aria-label="Default"
											aria-describedby="inputGroup-sizing-default"
											onChange={(e) => this.handleChange(e)}
											id={property}
											value={settings[property]}
									/>
								</InputGroup>
							</ListGroup.Item>
					break;
				case 'downloadSortBy':
					newInput =
							<ListGroup.Item key={property}>
								<InputGroup className="mb-3">
									<InputGroup.Text>{property}</InputGroup.Text>
									<Form.Control
											aria-label="Default"
											aria-describedby="inputGroup-sizing-default"
											as="select"
											onChange={(e) => this.handleChange(e)}>
											id={property}
											value={settings[property]}
										<option value="PROGRESS">Progress</option>
										<option value="STARTDATE">Date</option>
										<option value="NAME">Name</option>
									</Form.Control>
								</InputGroup>
							</ListGroup.Item>
					break;
					case 'downloadPath':
					newInput =
							<ListGroup.Item key={property}>
								<InputGroup className="mb-3">
									<InputGroup.Text>{property}</InputGroup.Text>
									<FormControl
											aria-label="Default"
											aria-describedby="inputGroup-sizing-default"
											onChange={(e) => this.handleChange(e)}
											id={property}
											value={settings[property]}
									/>
								</InputGroup>

							</ListGroup.Item>
					break;
			}
			newInput ? inputs.push(newInput) : null;
		}
		return inputs;
	}

	render() {
		const inputs = this.getInputs();
		return (
				<>
					<Col xs={12} md={5} style={{'margin': '15px'}}>
						<Form>
							<ListGroup variant="flush">
								{inputs}
							</ListGroup>
						</Form>
					</Col>
					<Card.Header>
						<Button variant="primary" onClick={(e) => this.handleSave(e)}>Save</Button>
					</Card.Header>
				</>
		)
	}
}
