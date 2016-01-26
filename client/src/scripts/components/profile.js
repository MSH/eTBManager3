
import React from 'react';

export default class Profile extends React.Component {

	/**
	 * Get the icon to display accoring to the type
	 * @return {string} name of the icon in the font awesome library
	 */
	getIconType() {
		switch (this.props.type) {
			case 'male':
			case 'female':
				return 'user';
			case 'tbunit':
				return 'hospital-o';
			case 'lab':
				return 'building';
			case 'ws':
				return 'globe';
			case 'medicine':
				return 'ticket';
			case 'product':
				return 'product-hunt';
			default:
				return 'exclamation-triangle';
		}
	}

	render() {
		let icon,
			imgClass;

		if (this.props.type) {
			imgClass = 'prof-' + this.props.type;
			icon = this.getIconType();
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

