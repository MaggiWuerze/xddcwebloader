import Server from "./ServerCard";

const React = require('react');
const ReactDOM = require('react-dom');


export default class ListView extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const servers = this.props.servers.map(server =>
            <Server key={server.id} server={server} onDelete={this.props.onDelete} onCreate={this.props.onCreate}/>
        );


        return (
            <div style={{'overflowY': 'auto', 'height': '-webkit-fill-available', 'paddingBottom': '15%'}}>
                {servers}
            </div>
        )
    }

}
