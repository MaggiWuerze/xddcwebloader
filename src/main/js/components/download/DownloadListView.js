import DownloadCard from './DownloadCard';

const React = require('react');
const ReactDOM = require('react-dom');


export default class DownloadListView extends React.Component {

	constructor(props) {
		super(props);
	}

	render() {
		const downloads = this.props.downloads.map(download =>
				<DownloadCard key={download.id} download={download} onCancel={this.props.onCancel} onDelete={this.props.onDelete}/>
		);


		return (
				<div className={'list'}>
					{downloads}
				</div>
		)
	}

}


