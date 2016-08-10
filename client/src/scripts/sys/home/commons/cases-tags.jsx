import React from 'react';
import { Badge } from 'react-bootstrap';
import { Card } from '../../../components';

export default class CasesTags extends React.Component {

	render() {
		return (
			<Card title={__('admin.tags')}>
				<div>
					{
						this.props.tags.map(item => (
							<a key={item.id} className={'tag-link tag-' + item.type.toLowerCase()} onClick={this.props.onClick(item)}>
								<Badge pullRight>{item.count}</Badge>
								<div className="tag-title">{item.name}</div>
							</a>
						))
					}
				</div>
			</Card>
			);
	}
}

CasesTags.propTypes = {
	tags: React.PropTypes.array,
	onClick: React.PropTypes.func
};
