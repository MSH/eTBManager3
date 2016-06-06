import React from 'react';

export default function BorderlessForm(props) {
	return (
		<ul className="list-group">
			{
				props.children.map((child, index) =>
					<li key={index} className="list-group-item group-borderless">
						{child}
					</li>
					)
			}
		</ul>
		);
}

BorderlessForm.propTypes = {
	children: React.PropTypes.any
};
