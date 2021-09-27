import Channel from "./ChannelCard";

const React = require('react');
const ReactDOM = require('react-dom');


export default class ListView extends React.Component {

	constructor(props) {
		super(props);
	}

	render() {
		const channels = this.props.channels.map(channel =>
				<Channel key={channel.id} channel={channel} onDelete={this.props.onDelete} onCreate={this.props.onCreate}/>
		);


		return (
				<div className="list">
					{channels}
				</div>
		)
	}

}
