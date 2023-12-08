import DownloadCard from './DownloadCard';
import Stack from 'react-bootstrap/Stack';

const React = require('react');
const ReactDOM = require('react-dom');


export default class DownloadListView extends React.Component {

	constructor(props) {
		super(props);
	}

	createEmptyView() {
		return (
		<div style={{height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
			<Stack style={{height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center'}} gap={1}>
				<div>
					<i className="fa-solid fa-sailboat fa-3x bobbing"></i>
				</div>
				<div>
					<p>All quiet…</p>
				</div>
			</Stack>
		</div>
		);
	}

	createDownloadList() {
		return this.props.downloads.map(download =>
				<DownloadCard key={download.id} download={download} onCancel={this.props.onCancel} onDelete={this.props.onDelete}/>);
	}

	render() {

		const viewContent = this.props.downloads.length === 0 ? this.createEmptyView() : this.createDownloadList();

		return (
				<div className={'list'}>
					{viewContent}
				</div>
		)
	}

}


