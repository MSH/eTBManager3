
import React from 'react';

export default class Profile extends React.Component {

	render() {
		const iconClass = 'fa fa-' + this.props.fa + ' fa-stack-1x profile-front';

		return (
			<div className="profile margin-2x">
				<div className="profile-image">
					<span className="fa-stack profile-image">
						<i className="fa fa-circle fa-stack-2x fa-inverse" />
						<i className={iconClass} />
					</span>
				</div>
				<div className="profile-title">{this.props.title}</div>
				<div>{this.props.subtitle}</div>
			</div>
		);
	}
}


Profile.propTypes = {
	fa: React.PropTypes.string,
	title: React.PropTypes.string,
	subtitle: React.PropTypes.any,
	size: React.PropTypes.string
};
