import BotCard from './BotCard';

const React = require('react');
const ReactDOM = require('react-dom');


export default class BotListView extends React.Component {

	constructor(props) {
		super(props);
	}

	render() {
		const bots = this.props.bots.map(bot =>
				<BotCard key={bot.id} bot={bot} onDelete={this.props.onDelete} onCreate={this.props.onCreate}/>
		);


		return (
				<div className="list">
					{bots}
				</div>
		)
	}

}
