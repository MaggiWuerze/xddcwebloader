'use strict';

import Tabs from 'react-bootstrap/Tabs';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/api';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {users: [], attributes: [], pageSize: 2, links: {}};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
    }

    // tag::follow-2[]
    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'users', params: {size: pageSize}}]
        ).then(userCollection => {
            return client({
                method: 'GET',
                path: userCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                return userCollection;
            });
        }).done(userCollection => {
            this.setState({
                users: userCollection.entity._embedded.users,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: userCollection.entity._links
            });
        });
    }

    // end::follow-2[]

    // tag::create[]
    onCreate(newUser) {
        follow(client, root, ['users']).then(userCollection => {
            return client({
                method: 'POST',
                path: userCollection.entity._links.self.href,
                entity: newUser,
                headers: {'Content-Type': 'application/json'}
            })
        }).then(response => {
            return follow(client, root, [
                {rel: 'users', params: {'size': this.state.pageSize}}]);
        }).done(response => {
            if (typeof response.entity._links.last !== "undefined") {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        });
    }

    // end::create[]

    // tag::delete[]
    onDelete(user) {
        client({method: 'DELETE', path: user._links.self.href}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    // end::delete[]

    // tag::navigate[]
    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(userCollection => {
            this.setState({
                users: userCollection.entity._embedded.users,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: userCollection.entity._links
            });
        });
    }

    // end::navigate[]

    // tag::update-page-size[]
    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }

    // end::update-page-size[]

    // tag::follow-1[]
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }

    // end::follow-1[]

    render() {
        return (
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>

                <Tabs defaultActiveKey="profile" id="uncontrolled-tab-example">
                    <Tab eventKey="home" title="Active Downloads">
                        <UserList users={this.state.users}
                                  links={this.state.links}
                                  pageSize={this.state.pageSize}
                                  onNavigate={this.onNavigate}
                                  onDelete={this.onDelete}
                                  updatePageSize={this.updatePageSize}/>

                    </Tab>
                    <Tab eventKey="profile" title="Profile">
                        <Sonnet/>
                    </Tab>
                    <Tab eventKey="contact" title="Contact" disabled>
                        <Sonnet/>
                    </Tab>
                </Tabs>;
            </div>
        )
    }
}

// tag::create-dialog[]
class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const newUser = {};
        this.props.attributes.forEach(attribute => {
            newUser[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newUser);

        // clear out the dialog's inputs
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>
            <p key={attribute}>
                <input type="text" placeholder={attribute} ref={attribute} className="field"/>
            </p>
        );

        return (
            <div>
                <a role="button" className="btn btn-success" href="#createUser">Create New User</a>

                <div id="createUser" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Create new user</h2>

                        <form>
                            {inputs}
                            <button type="button" className="btn btn-success" onClick={this.handleSubmit}>Create a
                                User
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }

}

// end::create-dialog[]

class UserList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    // tag::handle-page-size-updates[]
    handleInput(e) {
        e.preventDefault();
        const pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }

    // end::handle-page-size-updates[]

    // tag::handle-nav[]
    handleNavFirst(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }

    // end::handle-nav[]

    // tag::user-list-render[]
    render() {
        const users = this.props.users.map(user =>
            <User key={user._links.self.href} user={user} onDelete={this.props.onDelete}/>
        );

        const navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button className="btn btn-light" key="first"
                                  onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button className="btn btn-light" key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button className="btn btn-light" key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button className="btn btn-light" key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div className="table-responsive">
                <input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
                <table className="table">
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <th>Hash</th>
                        <th>Date</th>
                        <th></th>
                    </tr>
                    {users}
                    </tbody>
                </table>
                <div className="btn-group" role="group">
                    {navLinks}
                </div>
            </div>
        )
    }

    // end::user-list-render[]
}

// tag::user[]
class User extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.user);
    }

    render() {
        return (
            <tr>
                <td>{this.props.user.name}</td>
                <td>{this.props.user.password}</td>
                <td>{this.props.user.creationDate}</td>
                <td>
                    <div className="btn-group" role="group">
                        <button className="btn btn-outline-info">
                            <i className="far fa-info-circle"></i>
                        </button>
                        <button className="btn btn-outline-danger" onClick={this.handleDelete}>
                            <i className="far fa-trash-alt"></i>
                        </button>
                        {/*<button className="btn btn-danger" onClick={this.handleDelete}>*/}
                        {/*    <span className="fas fa-trash"/>*/}
                        {/*</button>*/}
                    </div>
                </td>
            </tr>
        )
    }
}

// end::user[]

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)
