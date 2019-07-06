import React, { Component } from 'react';
import {
    Alert,
    Button,
    Card,
    Carousel,
    Col,
    Container,
    FormControl,
    Form,
    InputGroup,
    Jumbotron,
    Row,
    Spinner
} from 'react-bootstrap';

const ReactDOM = require('react-dom');

export default class InitWizard extends React.Component {


    constructor(props) {
        super(props);

        // method refs
        this.handleFinish = this.handleFinish.bind(this);
        this.validateServer = this.validateServer.bind(this);
        this.validateChannel = this.validateChannel.bind(this);
        this.validateFileRef = this.validateFileRef.bind(this);
        this.validateBot = this.validateBot.bind(this);
        this.createServer = this.createServer.bind(this);
        this.createChannel = this.createChannel.bind(this);
        this.createBot = this.createBot.bind(this);
        this.nextPage = this.nextPage.bind(this);

        //field refs
        this.wizardServerForm = React.createRef();
        this.wizardServerBtn = React.createRef();

        this.wizardChannelForm = React.createRef();
        this.wizardChannelBtn = React.createRef();

        this.wizardBotForm = React.createRef();
        this.wizardBotBtn = React.createRef();

        this.state = {
            email: "",
            password: "",
            index: 0,
            direction: null,
            wrap: false,
            controls: false,
            interval: null,
            ServerValid: false,
            ChannelValid: false,
            BotValid: false,
            FileRefValid: false,
            workingInBg: false
        };
      }


    handleSelect(selectedIndex, e) {
        this.setState({
          index: selectedIndex,
        });
      }

    handleFinish(e) {

        console.log("finishing Wizard");
        if(this.state.FileRefValid){


            var fileRef = document.getElementById('wizardFormFileRef');
            console.log("with value " + fileRef);

            var download = {
                targetBotId: 1,
                fileRefId: fileRef.value
            }

            this.props.onCreate(download, "downloads", "", this.props.onFinish);

        }else{

            console.log("without setup");
            this.props.onFinish();

        }

    }



    //Form Validation
    checkValidity(input){

        this.resetValidity(input);

        if(!input.checkValidity()){

            this.setInvalid(input);

        }else{

            this.setValid(input);

        }

    }
    setInvalid(input){

        input.classList.add('is-invalid');
        input.classList.remove('is-valid');
        input.setCustomValidity("Invalid!");

    }
    setValid(input){

        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
        input.setCustomValidity("");

    }
    resetValidity(input){

        input.classList.remove('is-invalid');
        input.classList.remove('is-valid');
        input.setCustomValidity("");

    }
    validateServer(event){

        this.checkValidity(event.currentTarget);
        var validName = document.getElementById('wizardFormServerName');
        var validUrl = document.getElementById('wizardFormServerUrl');
        this.setState({ ServerValid: (validName.validity.valid && validUrl.validity.valid) });

    }
    validateChannel(event){

        this.checkValidity(event.currentTarget);
        var validName = document.getElementById('wizardFormChannelName');
        this.setState({ ChannelValid: validName.validity.valid });

    }
    validateBot(event){


        this.checkValidity(event.currentTarget);
        var validName = document.getElementById('wizardFormBotName');
        var validPattern = document.getElementById('wizardFormBotPattern');
        this.setState({ BotValid: (validName.validity.valid && validPattern.validity.valid) });

    }
    validateFileRef(event){

        var validFileRef = document.getElementById('wizardFormFileRef');
        this.checkValidity(event.currentTarget);

        console.log("checking fileRef with value:" + validFileRef.value);
        this.setState({ FileRefValid: validFileRef.validity.valid });
        console.log("fileRef valid:" + validFileRef.validity.valid);

    }

    nextPage(success){

        if(success){

            var nextIndex = this.state.index + 1;
            this.setState({
                index: nextIndex,
                workingInBg: false
            });

        }else{

           console.log("error!");
            this.setState({
                workingInBg: false,
                wizardError: true
            });

        }

    }

    //handle carousel page submit
    createServer(){

        this.setState({workingInBg: true});

        var validName = document.getElementById('wizardFormServerName').value;
        var validUrl = document.getElementById('wizardFormServerUrl').value;
        var server = {
            name: validName,
            serverUrl: validUrl
        }
        this.props.onCreate(server, "servers", "", this.nextPage);

    }
    createChannel(){

        this.setState({workingInBg: true});

        var validName = document.getElementById('wizardFormChannelName').value;
        var channel = {
            name: validName,
            bla: "valid"
        }
        this.props.onCreate(channel, "channels", "", this.nextPage);

    }

    createBot(){

        this.setState({workingInBg: true});

        var validName = document.getElementById('wizardFormBotName').value;
        var validPattern = document.getElementById('wizardFormBotPattern').value;
        var bot = {
            name: validName,
            pattern: validPattern,
            serverId: 1,
            channelId: 1
        }

        this.props.onCreate(bot, "bots", "", this.nextPage);

    }

    render(){

        return (

            <section id="welcome" style={{minHeight: '100vh'}} className="bg-primary d-flex flex-column">
                <div className="container text-center my-auto">
                    <Carousel
                        as='container'
                        activeIndex={this.state.index}
                        direction={this.state.direction}
                        onSelect={this.handleSelect}
                        wrap={this.state.wrap}
                        controls={this.state.controls}
                        interval={this.state.interval}
                        indicators={false}
                        slide={false}
                        className="bg-light"
                        style={{minHeight: '40vh'}}>

                        {/*WELCOME PAGE*/}
                        <Carousel.Item className="wizardPageContainer">
                            <Row>
                                <Col>
                                    <h1>Welcome</h1>
                                    <p>On the following pages you can set up your first xdcc bot.</p>
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Button style={{width: 105 + 'px', marginRight:50 + 'px'}} variant={'danger'} onClick={this.handleFinish}>Skip</Button>
                                <Button style={{width: 105 + 'px', marginLeft:50 + 'px'}} variant={'light'} onClick={() => this.setState({index: 1})}>Start</Button>
                            </Row>
                        </Carousel.Item>

                        {/*FIRST PAGE*/}
                        <Carousel.Item className="wizardPageContainer">

                        {this.state.workingInBg &&

                                <section id="welcome" style={{minHeight: '100vh'}} className="bg-primary d-flex flex-column">
                                    <div className="container text-center my-auto">
                                        <Spinner className="wizardSpinner" animation="border" role="status">
                                          <span className="sr-only">Creating Server...</span>
                                        </Spinner>
                                    </div>
                                </section>

                        }
                        {!this.state.workingInBg &&
                                <>
                                <Row>
                                    <Col>
                                        <h2>The Server</h2>
                                        <p>Enter the URL to the IRC server you want to connect to</p>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <InputGroup className="mb-3">
                                            <InputGroup.Prepend>
                                              <InputGroup.Text id="basic-addon1">Servername</InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <Form.Control
                                                id="wizardFormServerName"
                                                required
                                                type="text"
                                                onChange={this.validateServer}
                                                placeholder="The name of your Server, e.g. 'Rizon'"
                                                aria-label="Username"
                                                aria-describedby="basic-addon1"
                                            />
                                            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                                            <Form.Control.Feedback type="invalid">Please provide a name for your Server.</Form.Control.Feedback>
                                        </InputGroup>

                                        <InputGroup className="mb-3">
                                            <InputGroup.Prepend>
                                              <InputGroup.Text id="basic-addon1">Server URL</InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <Form.Control
                                                id="wizardFormServerUrl"
                                                required
                                                type="text"
                                                onChange={this.validateServer}
                                                placeholder="The Server URL, e.g. 'irc.rizon.net'"
                                                aria-label="Username"
                                                aria-describedby="basic-addon1"
                                            />
                                            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                                            <Form.Control.Feedback type="invalid">Please provide a url.</Form.Control.Feedback>
                                        </InputGroup>
                                        {this.state.wizardError &&
                                            <Alert variant={'danger'}>
                                                There was an error while creating your Server. Please inspect your values and try again
                                            </Alert>
                                        }
                                        <Button variant={'light'} ref={this.wizardServerBtn} disabled={!this.state.ServerValid} onClick={this.createServer}>Next Page</Button>
                                    </Col>


                                </Row>
                                </>
                        }
                        </Carousel.Item>

                        {/*  SECOND PAGE */}
                        <Carousel.Item className="wizardPageContainer">

                            {this.state.workingInBg &&
                                <section id="welcome" style={{minHeight: '100vh'}} className="bg-primary d-flex flex-column">
                                    <div className="container text-center my-auto">
                                        <Spinner animation="border" role="status">
                                          <span className="sr-only">Adding Channel...</span>
                                        </Spinner>
                                    </div>
                                </section>
                            }
                            {!this.state.workingInBg &&

                                <>
                                    <h2>Add A Channel</h2>
                                    <p>
                                      Now add a channel that exists on the server you just entered.<br />
                                      This is the channel where you want to request your files.
                                    </p>
                                    <InputGroup className="mb-3">
                                      <InputGroup.Prepend>
                                        <InputGroup.Text id="basic-addon2">Channelname</InputGroup.Text>
                                      </InputGroup.Prepend>
                                      <Form.Control
                                        id="wizardFormChannelName"
                                        required
                                        type="text"
                                        onChange={this.validateChannel}
                                        placeholder="Name of the IRC Channel, e.g. '#Lobby' or '#HorribleSubs'"
                                        aria-label="Username"
                                        aria-describedby="basic-addon2"
                                      />
                                      <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                                      <Form.Control.Feedback type="invalid">
                                              Please provide a valid name for your Channel.
                                      </Form.Control.Feedback>
                                    </InputGroup>
                                    {this.state.wizardError &&
                                        <Alert variant={'danger'}>
                                            There was an error while adding your Channel. Please inspect your values and try again
                                        </Alert>
                                    }
                                    <Button variant={'light'} ref={this.wizardChannelBtn} disabled={!this.state.ChannelValid} onClick={this.createChannel}>Next Page</Button>
                                </>
                            }
                        </Carousel.Item>

                        {/*  THIRD PAGE */}
                        <Carousel.Item className="wizardPageContainer">
                            {this.state.workingInBg &&
                                <section id="welcome" style={{minHeight: '100vh'}} className="bg-primary d-flex flex-column">
                                    <div className="container text-center my-auto">
                                        <Spinner animation="border" role="status">
                                          <span className="sr-only">Adding Channel...</span>
                                        </Spinner>
                                    </div>
                                </section>
                            }
                            {!this.state.workingInBg &&

                                <>
                                    <h2>Create Your Bot</h2>
                                    <p>
                                        Finally enter the name of the bot that serves your content as well as a message template that will be used to write messages to them.
                                        The message template should look somewhat like this: <code> 'xdcc send %s' </code><br />
                                        The <code>'%s'</code> part is where the part that identifies the file will be placed.
                                        In some cases this is something like an id (#3432) in other cases it may be the specific file name (yourfile.txt).<br />
                                        It is <b>mandatory</b> to include the <code>'%s'</code>
                                    </p>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Prepend>
                                          <InputGroup.Text id="basic-addon3">Botname</InputGroup.Text>
                                        </InputGroup.Prepend>
                                        <FormControl
                                            id="wizardFormBotName"
                                            required
                                            type="text"
                                            onChange={this.validateBot}
                                            placeholder="The name of the bot you want to send your requests to, e.g. 'Ginpachi-Sensei'"
                                            aria-label="Username"
                                            aria-describedby="basic-addon3"
                                        />
                                    </InputGroup>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Prepend>
                                          <InputGroup.Text id="basic-addon4">Messagepattern</InputGroup.Text>
                                        </InputGroup.Prepend>
                                        <Form.Control
                                            id="wizardFormBotPattern"
                                            required
                                            type="text"
                                            pattern=".*(%s).*"
                                            onChange={this.validateBot}
                                            placeholder="A message template that will be used to send messages, e.g. 'xdcc send %s'"
                                            aria-label="Messagepattern"
                                            aria-describedby="basic-addon4"
                                        />
                                        <Form.Control.Feedback type="invalid">
                                              Please provide a valid state.
                                        </Form.Control.Feedback>
                                    </InputGroup>
                                    <Button variant={'light'} ref={this.wizardBotBtn} disabled={!this.state.BotValid} onClick={this.createBot}>Next Page</Button>
                                    {this.state.wizardError &&
                                        <Alert variant={'danger'}>
                                            There was an error while creating your Bot. Please inspect your values and try again
                                        </Alert>
                                    }
                                </>
                            }
                        </Carousel.Item >

                        {/*  FINAL PAGE */}
                        <Carousel.Item className="wizardPageContainer">
                            <Row>
                                <Col>
                                    <h2>That{"\'"}s it!</h2>
                                    <p>Now your bot is ready to get files for you.</p>
                                    <h4>Let{"\'"} try it.</h4>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Prepend>
                                            <InputGroup.Text id="basic-addon5">
                                                File reference
                                            </InputGroup.Text>
                                        </InputGroup.Prepend>
                                        <FormControl
                                            required
                                            id="wizardFormFileRef"
                                            placeholder="the ID/Name referencing your File"
                                            aria-label="the ID or Name referencing your File"
                                            aria-describedby="basic-addon5"
                                            onChange={this.validateFileRef}
                                        />
                                    </InputGroup>

                                    {!this.state.FileRefValid &&
                                        <Button variant={'danger'} onClick={this.handleFinish}>No Thanks!</Button>
                                    }

                                    {this.state.FileRefValid &&
                                        <Button variant={'success'} onClick={this.handleFinish}>Send</Button>
                                    }

                                </Col>
                            </Row>
                        </Carousel.Item>
                    </Carousel>
                    </div>
                </section>
            );
    }

}