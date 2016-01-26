
import React from 'react';

export default class Profile extends React.Component {


	render() {
		let icon,
			imgClass;

		if (this.props.type) {
			imgClass = 'prof-' + this.props.type;
			switch (this.props.type) {
				case 'male':
				case 'female':
					icon = 'user';
					break;
				case 'tbunit':
					icon = 'hospital-o';
					break;
				case 'lab':
					icon = 'building';
					break;
				case 'ws':
					icon = 'globe';
					break;
				default:
					icon = 'user';
					imgClass = null;
			}
		}
		else {
			icon = this.props.fa;
			imgClass = this.props.imgClass;
		}

		icon = 'fa fa-' + icon + ' profile-front';
		imgClass = 'profile-image ' + (imgClass ? imgClass : 'profile-img-default');

		const profileClass = 'profile profile-' + (this.props.size || 'medium');

		return (
			<div className={profileClass}>
				<div className={imgClass}>
					<i className={icon} />
				</div>
				<div className="profile-title">{this.props.title}</div>
				<div className="profile-subtitle">{this.props.subtitle}</div>
			</div>
		);
	}
}


Profile.propTypes = {
	fa: React.PropTypes.string,
	title: React.PropTypes.string,
	subtitle: React.PropTypes.any,
	size: React.PropTypes.string,
	imgClass: React.PropTypes.string,
	type: React.PropTypes.oneOf(['male', 'female', 'tbunit', 'lab', 'ws', 'product', 'medicine'])
};

