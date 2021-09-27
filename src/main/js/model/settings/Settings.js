import React from 'react';
import {Button, Card, Col, Form, ListGroup} from 'react-bootstrap';

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
		this.settingsForm = React.createRef();
		this.resetBtn = React.createRef();
		this.saveBtn = React.createRef();

		this.state = {
			settings: this.props.userSettings,
			settingsChanged: false
		};


	}

	handleChange(event) {

		if (event.target.id) {

			let value = event.target.checked;
			let updatedSettings = this.state.settings;
			updatedSettings[event.target.id] = value;
			this.setState({settings: updatedSettings});

		}
	}

	settingsChanged() {

		return false;

	}

	handleSave(e) {
		if (this.settingsChanged()) {
			var fileRef = document.getElementById('wizardFormFileRef');
			var download = {
				targetBotId: 1,
				fileRefId: fileRef.value
			}
			this.props.onCreate(download, "downloads", "", this.props.onFinish);
		} else {
			this.props.onFinish();
		}
	}

	handleReset(e) {

		console.log("resetting settings");
		if (this.settingsChanged()) {


			var fileRef = document.getElementById('wizardFormFileRef');
			console.log("with value " + fileRef);

			var download = {
				targetBotId: 1,
				fileRefId: fileRef.value
			}

			this.props.onCreate(download, "downloads", "", this.props.onFinish);

		} else {

			console.log("without setup");
			this.props.onFinish();

		}

	}

	getInputs() {

		var settings = this.state.settings;
		var inputs = [];
		for (var property in settings) {

			var newInput = null;

			switch (property) {

				case 'refreshrateInSeconds':

					break;

				case 'sessionTimeout':

					break;

				case 'downloadSortBy':

					break;

				case 'showAllBotsInQuickWindow':

					const inputFields = [];
					console.log(settings['botsVisibleInQuickWindow']);
					settings['botsVisibleInQuickWindow']
					    .forEach(function(bot) {
					        inputFields.push(
					            <InputGroup className="mb-3">
					                <FormControl disabled value={bot.name} />
					                <InputGroup.Append>
					                    <InputGroup.Checkbox
					                        type="checkbox"
					                        id= {bot.id}
					                    />
					                </InputGroup.Append>
					            </InputGroup>)
					    });

					newInput =
					    <>
					        <h4>Bots available in quickview</h4>
					        <InputGroup className="mb-3">
					            <FormControl disabled value={property} />
					            <InputGroup.Append>
					                <InputGroup.Checkbox
					                    onChange={(e) => this.handleChange(e)}
					                    type="checkbox"
					                    id= {property}
					                />
					            </InputGroup.Append>
					        </InputGroup>

					        {!this.state.settings.showAllBotsInQuickWindow &&
					            <ListGroup.Item className="mb-3">
					                {inputFields}
					            </ListGroup.Item>
					        }
					    </>

					break;

				case 'showAllItemsInDownloadCard':

					break;

			}

			newInput ? inputs.push(newInput) : null;
		}

		return inputs;

	}

	//Form Validation
	checkValidity(input) {

		this.resetValidity(input);

		if (!input.checkValidity()) {

			this.setInvalid(input);

		} else {

			this.setValid(input);

		}

	}

	render() {

		const inputs = this.getInputs();

		return (

				<Col xs={12} md={10} className={"column"}>
					<Card>
						<Card.Header>Settings</Card.Header>
						<Col xs={12} md={5} style={{'margin': '15px'}}>
							<Form>
								<ListGroup variant="flush">
									{inputs}
								</ListGroup>
							</Form>
						</Col>
						<Card.Header>
							<Button variant="primary">Save</Button>
						</Card.Header>
					</Card>
				</Col>
		)
	}

}
