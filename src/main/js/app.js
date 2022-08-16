'use strict';
import { createRoot } from "react-dom/client";
import BotListView from './model/bot/BotListView';
import DownloadListView from './model/download/DownloadListView';
import ServerListView from './model/server/ServerListView';
import ChannelListView from './model/channel/ChannelListView';
import BotInputs from './model/bot/BotInputs';
import ServerInputs from './model/server/ServerInputs';
import ChannelInputs from './model/channel/ChannelInputs';
import InitWizard from './model/wizard/InitWizard';
import Settings from './model/settings/Settings';

import {Button, Card, Col, Container, Nav, Row, Tab, Toast, TabContent, ToastContainer} from 'react-bootstrap';
import Popover from 'react-popover';

import axios from 'axios';

const React = require('react');
const root = createRoot(document.getElementById("react"));
const stompClient = require('./websocket-listener');

const version_tag = "V. 0.5";

class App extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			initialized: true,
			user: null,
			bots: [],
			servers: [],
			channels: [],
			downloads: [],
			doneDownloads: [],
			failedDownloads: [],
			botAttributes: [],
			channelAttributes: [],
			serverAttributes: [],
			toasts: [],
			links: {},
			activePage: 'dashboard',
			showBotPopover: false,
			showServerPopover: false,
			showChannelPopover: false
		};
		this.onCreate = this.onCreate.bind(this);
		this.onDelete = this.onDelete.bind(this);
		this.onCancel = this.onCancel.bind(this);
		this.handleSocketCall = this.handleSocketCall.bind(this);
		this.toggleBoolean = this.toggleBoolean.bind(this);
		this.finishOnboarding = this.finishOnboarding.bind(this);
		this.onMenuInteraction = this.onMenuInteraction.bind(this);

	}

	loadFromServer() {
		this.loadInitializedState();
		this.loadUserData();
		this.loadAttributes();
		this.loadBotData();
		this.loadServerData();
		this.loadChannelData();
		this.loadDownloadData();
	}

	loadInitializedState() {
		// INIT
		axios
				.get('initialized/')
				.then((response) => {
					const init = response.data;
					console.debug("user is initialized: " + init)
					this.setState({
						initialized: init
					});
				})
				.catch((error) => {
					console.debug(error);
				});
	}

	// USER
	loadUserData() {
		axios
				.get('user/')
				.then((response) => {
					var userObject = response.data;
					this.setState({
						user: userObject
					});
				})
				.catch((error) => {
					console.debug(error);
				});
	}

	loadAttributes() {
		axios
				.get('bots/example', {})
				.then((response) => {
					response.data[0] ? this.updateAttributes(Object.keys(response.data[0]), 'botAttributes') : null;
				})
				.catch((error) => {
					console.debug(error);
				});
		axios
				.get('servers/example', {})
				.then((response) => {
					response.data[0] ? this.updateAttributes(Object.keys(response.data[0]), 'serverAttributes') : null;
				})
				.catch((error) => {
					console.debug(error);
				});
		axios
				.get('channels/example', {})
				.then((response) => {
					response.data[0] ? this.updateAttributes(Object.keys(response.data[0]), 'channelAttributes') : null;
				})
				.catch((error) => {
					console.debug(error);
				});
	}

	//BOTS,SERVERS,CHANNELS
	loadBotData() {
		axios
				.get('bots/', {
					params: {
						active: true,
					}
				})
				.then((response) => {
					this.setState({
						bots: response.data
					});
				})
				.catch((error) => {
					console.debug(error);
				});
	}

	loadServerData() {
		axios
				.get('servers/')
				.then((response) => {
					this.setState({
						servers: response.data
					});
				})
				.catch((error) => {
					console.debug(error);
				});
	}

	loadChannelData() {
		axios
				.get('channels/')
				.then((response) => {
					this.setState({
						channels: response.data
					});
				})
				.catch((error) => {
					console.debug(error);
				});
	}

	loadDownloadData() {
		//DOWNLOADS
		axios
				.get('downloads/active/', {
					params: {
						active: true,
					}
				})
				.then((response) => {
					this.setState({
						downloads: response.data
					});
				})
				.catch((error) => {
					console.debug(error);
				});

		axios
				.get('downloads/failed')
				.then((response) => {
					this.setState({
						failedDownloads: response.data
					});
				})
				.catch((error) => {
					console.debug(error);
				});

		axios
				.get('downloads/active/', {
					params: {
						active: false,
					}
				})
				.then((response) => {
					this.setState({
						doneDownloads: response.data
					});
				})
				.catch((error) => {
					console.debug(error);
				});
	}

	onCreate(object, objectType, modalName, callback) {
		console.debug("onCreate with: {object}, {objectType}, {modalName}, {callback}")
		axios
				.post(objectType + '/', object)
				.then((response) => {
					switch (response.status.toString()) {
						case '200':
							modalName ? this.toggleBoolean(modalName) : '';
							callback ? callback(true) : '';
							break;

						default:
							console.debug("onCreate error");
							console.debug("statuscode: " + response.status.toString());
							callback ? callback(false) : '';
					}
				}).catch((error) => {
			callback ? callback(false) : '';
			console.debug(error);
		});
	}

	onDelete(payload, type) {
		if (payload && type) {
			const id = payload.id;
			switch (type) {
				case 'DL':
					axios
						.delete('downloads/', {
							params: {
								downloadId: payload.id,
							}
						})
						.then((response) => {

							switch (response.status.toString()) {
								case '200':
									this.setState({downloads: this.state.downloads.filter(item => item.id !== id)});
									this.setState({doneDownloads: this.state.doneDownloads.filter(item => item.id !== id)});
									this.setState({failedDownloads: this.state.failedDownloads.filter(item => item.id !== id)});
									break;

								default:
									console.debug("onDelete error");
									console.debug("statuscode: " + response.status.toString());
									callback ? callback(false) : '';
							}
						}).catch((error) => {
							console.debug(error);
						});
					break;
				case 'BOT':
					console.debug("deleting BOT")
					axios
							.delete('bots/', {
								params: {
									botId: payload.id,
								}
							})
							.then((response) => {
								switch (response.status.toString()) {
									case '200':
										this.setState({bots: this.state.bots.filter(item => item.id !== id)});
										break;
									default:
										console.debug("onDelete error");
										console.debug("statuscode: " + response.status.toString());
										callback ? callback(false) : '';
										this.showErrorToast("Error Deleting Bot!", response.data)
								}
							}).catch((error) => {
								this.showErrorToast("Error Deleting Bot!", error.response.data);
					});
				break;
				case 'CH':
					console.debug("deleting CHANNEL")
					axios
							.delete('channels/', {
								params: {
									channelId: payload.id,
								}
							})
							.then((response) => {

								switch (response.status.toString()) {
									case '200':
										this.setState({channels: this.state.channels.filter(item => item.id !== id)});
										break;

									default:
										console.debug("onDelete error");
										console.debug("statuscode: " + response.status.toString());
										callback ? callback(false) : '';
										this.showErrorToast("Error Deleting Server!", response.data)
								}
							}).catch((error) => {
								if(error.response.status == 409) {
									this.showWarnToast("This Channel is currently in use.", "Delete the respective Bot before deleting its children.")
								} else {
									this.showErrorToast("Error Deleting Channel!", error.response.data)
								}
					});
				break;
				case 'SRV':
					console.debug("deleting SERVER")
					axios
							.delete('servers/', {
								params: {
									serverId: payload.id,
								}
							})
							.then((response) => {

								switch (response.status.toString()) {
									case '200':
										this.setState({servers: this.state.servers.filter(item => item.id !== id)});
										break;

									case '409':
										this.showErrorToast(error.toString())
										break;
									default:
										console.debug("onDelete error");
										console.debug("statuscode: " + response.status.toString());
										callback ? callback(false) : '';
										this.showErrorToast("Error Deleting Server!", response.data)
								}
							}).catch((error) => {
								if(error.response.status == 409) {
									this.showWarnToast("This Server is currently in use.", "Delete the respective Bot before deleting its children.")
								} else {
									this.showErrorToast("Error Deleting Server!", error.response.data)
								}
					});
				break;
				default:
			}
		}
	}

	showWarnToast(title, message) {
		this.showToast(title, message, 'Warning');
	}
	showErrorToast(title, message) {
		this.showToast(title, message, 'Danger');
	}
	showToast(title, message, variant) {
		this.setState(prevState => ({
			toasts: [...prevState.toasts, {title: title, variant:variant, message: message}]
		}));
	}

	hideToast(toast){
		this.setState({toasts: this.state.toasts.filter(item => item !== toast)});
	}

	onCancel(payload, type) {
		// client({method: 'DELETE', path: payload._links.self.href}).done(response => {
		//     this.loadFromServer(this.state.pageSize);
		// });
	}

	onMenuInteraction(eventKey, event) {
		this.setState({
			activePage: eventKey
		});
	}

	moveToList(sourceList, targetList, item) {
		this.removeFromListById(sourceList, item.id);
		return this.addToListAndSort(targetList, item);
	}

	addToListAndSort(list, newItem) {
		// this.setState(state => {
		const downloads = list;
		var containsDownload = false;

		if (!downloads.some(e => e.id === newItem.id)) {
			/* downloads contains an item with the same id */
			downloads.push(newItem);
			downloads.sort(function(a, b) {
				//multidimension sort?
				return a.progress - b.progress || a.date - b.date;
			});
		}

		return downloads;
	}

	removeFromListById(targetList, idToRemove) {
		var list = targetList.filter(item => item.id !== idToRemove);
		return list;
	}

	updateItemAndSort(list, newItem) {
		this.setState(state => {
			const downloads = list.map((item) => {
				if (item.id == newItem.id) {
					return newItem;
				} else {
					return item;
				}
			});

			downloads.sort(function(a, b) {
				//multidimension sort?
				return (a.progress > b.progress) ? -1 : (a.progress < b.progress) ? 1 : 0;
				// return a.progress - b.progress || a.date - b.date;
			});
			return {downloads,};
		});
	}

	updateAttributes(attributes, paramName) {
		if (this.state[paramName] && this.state[paramName].length <= 0) {
			this.setState({[paramName]: attributes});
		}
	}

	handleSocketCall(responseObj) {
		let message = JSON.parse(responseObj.body);
		let downloads = this.state.downloads;
		let failedDownloads = this.state.failedDownloads;
		let doneDownloads = this.state.doneDownloads;

		switch (responseObj.headers.destination) {
			case '/topic/newDownload':
				this.setState({
					downloads: this.addToListAndSort(downloads, message)
				});
				break;

			case '/topic/updateDownload':

				if (message.status == 'DONE') {
					this.setState({
						downloads: this.removeFromListById(downloads, message.id),
						doneDownloads: this.addToListAndSort(doneDownloads, message)
					});
					// this.moveToList(downloads, doneDownloads, message);

				} else if (message.status == 'ERROR') {
					this.setState({
						downloads: this.removeFromListById(downloads, message.id),
						failedDownloads: this.addToListAndSort(failedDownloads, message)
					});
				} else {
					this.updateItemAndSort(downloads, message);
				}
				break;

			case '/topic/deleteDownload':
				this.setState({
					downloads: this.removeFromListById(downloads, message.id),
					doneDownloads: this.removeFromListById(doneDownloads, message.id),
					failedDownloads: this.removeFromListById(failedDownloads, message.id)
				});
				break;

			case '/topic/timeout':
				console.debug("your session has timed out. please log in again");
				window.location.href = "login?timeout=true";
				break;

			case '/topic/error':
				console.debug("There was an error!");
				this.showErrorToast("Server Error",message);
				break;

			default:
				console.debug("unknown event route! destination was: " + responseObj.headers.destination);
		}


	}

	toggleBoolean(key) {
		this.setState({[key]: !this.state[key]});
	}

	finishOnboarding(setupDone) {
		axios
				.post('initialized/')
				.then((response) => {
					switch (response.status.toString()) {
						case '200':
							this.toggleBoolean('initialized');
							let newUrl = location.replace("register", "");
							let title = "XDCC Loader"
							console.debug("newUrl: " + newUrl)
							console.debug("title: " + title)
							var obj = {Title: title, Url: newUrl};
							history.pushState(obj, obj.Title, obj.Url);
							break;

						default:
							console.debug("finisch onboarding error");
							console.debug("statuscode: " + response.status.toString());
					}
				}).catch((error) => {
			console.debug(error);
		});

		setupDone ? this.loadFromServer() : "";
	}

	componentDidMount() {

		this.loadFromServer();
		stompClient.register([
			{route: '/topic/newDownload', callback: this.handleSocketCall},
			{route: '/topic/updateDownload', callback: this.handleSocketCall},
			{route: '/topic/deleteDownload', callback: this.handleSocketCall},

			{route: '/topic/newBot', callback: this.handleSocketCall},
			{route: '/topic/updateBot', callback: this.handleSocketCall},
			{route: '/topic/deleteBot', callback: this.handleSocketCall},

			{route: '/topic/newServer', callback: this.handleSocketCall},
			{route: '/topic/updateServer', callback: this.handleSocketCall},
			{route: '/topic/deleteServer', callback: this.handleSocketCall},

			{route: '/topic/newChannel', callback: this.handleSocketCall},
			{route: '/topic/updateChannel', callback: this.handleSocketCall},
			{route: '/topic/deleteChannel', callback: this.handleSocketCall},

			{route: '/topic/timeout', callback: this.handleSocketCall}
		]);
	}

	render() {
		const botPopover = (
				<BotInputs modaltitle="Create new Bot" attributes={this.state.botAttributes}
						show={this.state.showBotModal} onFinish={() => this.loadBotData()}
						onCreate={this.onCreate}/>
		);

		const serverPopover = (
				<ServerInputs modaltitle="Create new Bot" attributes={this.state.serverAttributes}
						show={this.state.showBotModal} onFinish={() => this.loadServerData()}
						onCreate={this.onCreate}/>
		);

		const channelPopover = (
				<ChannelInputs modaltitle="Create new Bot" attributes={this.state.channelAttributes}
						show={this.state.showBotModal} onFinish={() => this.loadChannelData()}
						onCreate={this.onCreate}/>
		);

		const toasts = this.state.toasts.map((toast, idx) =>
				<Toast
						onClose={() => this.hideToast(toast)}
						className="d-inline-block m-1"
						bg={toast.variant.toLowerCase()}
						key={idx}
						delay={5000}
						autohide>
					<Toast.Header>
						{toast.title}
					</Toast.Header>
					<Toast.Body>
						{toast.message}
					</Toast.Body>
				</Toast>
		);

		if (!this.state.initialized) {
			return (
					<InitWizard onCreate={this.onCreate} onFinish={this.finishOnboarding}/>
			)
		} else {
			return (
					<React.Fragment>
						<Container fluid>
							<ToastContainer position='top-end' style={{margin: '1rem' , zIndex: '999'}}>
								{toasts}
							</ToastContainer>
							<Row>
								<Col xs={12} md={1} className="sidenav-column">
									<Card className="text-center" className="sidenav">
										<Card.Body className="text-muted sidenav-body">
											<Nav justify defaultActiveKey="dashboard" onSelect={this.onMenuInteraction} className="flex-column">
												<Nav.Link as="span" eventKey="dashboard">
													<i className="fas fa-columns"></i>
													&nbsp;&nbsp;Dashboard
												</Nav.Link>
												<Nav.Link as="span" eventKey="settings">
													<i className="fas fa-sliders-h"></i>
													&nbsp;&nbsp;Settings
												</Nav.Link>
												<Nav.Link as="span" eventKey="about">
													<i className="fas fa-info-circle"></i>
													&nbsp;&nbsp;About
												</Nav.Link>
											</Nav>
										</Card.Body>
										<Card.Footer className="text-muted sidenav-footer">
											<div className="footerIconWrapper">
												<a href="/logout">
													<i title="Log out" className="fa-solid fa-right-from-bracket fa-2x"></i>
												</a>
												<a target="_blank" href="https://github.com/MaggiWuerze/xddcwebloader">
													<i title={version_tag} className="fab fa-github fa-2x"></i>
												</a>
											</div>
										</Card.Footer>
									</Card>
								</Col>

								{/* DASHBOARD */}
								{this.state.activePage == "dashboard" &&
								<>
									<Col xs={12} md={4} className={"column"}>
										<Card className={"customCard"}>
											<Tab.Container defaultActiveKey="bots">
												<Card.Header>
													<Nav fill variant="tabs">
														<Nav.Item>
															<Nav.Link eventKey="bots">
                                                        <span>
                                                            {"Bots (" + this.state.bots.length + ")"}&nbsp;
															<Popover
																	isOpen={this.state.showBotPopover}
																	body={botPopover}
																	place="below"
																	enterExitTransitionDurationMs={300}
																	onOuterAction={() => this.toggleBoolean('showBotPopover')}>
                                                                <Button size="sm" className={"tab_btn"} variant="success"
																		onClick={() => this.toggleBoolean('showBotPopover')}>
                                                                    <i className="fas fa-plus"></i>
                                                                </Button>
                                                             </Popover>
                                                        </span>
															</Nav.Link>
														</Nav.Item>
														<Nav.Item>
															<Nav.Link eventKey="servers">
                                                        <span>
                                                            {"Servers (" + this.state.servers.length + ")"}&nbsp;
															<Popover
																	isOpen={this.state.showServerPopover}
																	body={serverPopover}
																	place="below"
																	enterExitTransitionDurationMs={300}
																	onOuterAction={() => this.toggleBoolean('showServerPopover')}>
                                                                <Button size="sm" className={"tab_btn"} variant="success"
																		onClick={() => this.toggleBoolean('showServerPopover')}>
                                                                    <i className="fas fa-plus"></i>
                                                                </Button>
                                                             </Popover>
                                                        </span>
															</Nav.Link>
														</Nav.Item>
														<Nav.Item>
															<Nav.Link eventKey="channels">
                                                        <span>
                                                            {"Channels (" + this.state.channels.length + ")"}&nbsp;
															<Popover
																	isOpen={this.state.showChannelPopover}
																	body={channelPopover}
																	place="below"
																	enterExitTransitionDurationMs={300}
																	onOuterAction={() => this.toggleBoolean('showChannelPopover')}>
                                                                <Button size="sm" className={"tab_btn"} variant="success"
																		onClick={() => this.toggleBoolean('showChannelPopover')}>
                                                                    <i className="fas fa-plus"></i>
                                                                </Button>
                                                             </Popover>
                                                        </span>
															</Nav.Link>
														</Nav.Item>
													</Nav>
												</Card.Header>
												<Card.Body>
													<TabContent>
														<Tab.Pane eventKey="bots">
															<BotListView
																	bots={this.state.bots}
																	onDelete={this.onDelete}
																	onCreate={this.onCreate}/>
														</Tab.Pane>
														<Tab.Pane eventKey="servers">
															<ServerListView
																	servers={this.state.servers}
																	onDelete={this.onDelete}
																	onCreate={this.onCreate}/>
														</Tab.Pane>
														<Tab.Pane eventKey="channels">
															<ChannelListView
																	channels={this.state.channels}
																	onDelete={this.onDelete}
																	onCreate={this.onCreate}/>
														</Tab.Pane>
													</TabContent>
												</Card.Body>
											</Tab.Container>
										</Card>

									</Col>
									<Col xs={12} md={7} className={"column"}>
										<Card className={"customCard"}>
											<Tab.Container defaultActiveKey="activeDownloads">
												<Card.Header>
													<Nav fill variant="tabs">
														<Nav.Item>
															<Nav.Link
																	eventKey="activeDownloads">{"Active Downloads (" + this.state.downloads.length + ")"}</Nav.Link>
														</Nav.Item>
														<Nav.Item>
															<Nav.Link
																	eventKey="completedDownloads">{"Completed (" + this.state.doneDownloads.length + ")"}</Nav.Link>
														</Nav.Item>
														<Nav.Item>
															<Nav.Link
																	eventKey="failedDownloads">{"Failed (" + this.state.failedDownloads.length + ")"}</Nav.Link>
														</Nav.Item>
													</Nav>
												</Card.Header>
												<Card.Body>
													<TabContent>
														<Tab.Pane eventKey="activeDownloads">
															<DownloadListView
																	downloads={this.state.downloads}
																	onDelete={this.onDelete}
																	onCancel={this.onCancel}
															/>
														</Tab.Pane>
														<Tab.Pane eventKey="completedDownloads">
															<DownloadListView downloads={this.state.doneDownloads}
																	onDelete={this.onDelete}/>
														</Tab.Pane>
														<Tab.Pane eventKey="failedDownloads">
															<DownloadListView downloads={this.state.failedDownloads}
																	onDelete={this.onDelete}/>
														</Tab.Pane>
													</TabContent>
												</Card.Body>
											</Tab.Container>
										</Card>
									</Col>
								</>
								}

								{/* SETTINGS */}
								{this.state.activePage == 'settings' && <>
									<Col xs={12} md={10} className={"column"}>
										<Settings userSettings={this.state.user.userSettings} onCreate={this.onCreate}/>
									</Col>
								</>}

								{/* ABOUT */}
								{this.state.activePage == 'about' && <>
									<Col xs={12} md={10} className={"column"}>
										<div className="jumbotron">
											<h1>About XDCC Webloader</h1>
											<p>
												Information about the project
											</p>
											<p>
												<Button variant="primary">Learn more</Button>
											</p>
										</div>
									</Col>
								</>}
							</Row>
						</Container>
					</React.Fragment>
			)
		}
	}
}

root.render(<App/>)
