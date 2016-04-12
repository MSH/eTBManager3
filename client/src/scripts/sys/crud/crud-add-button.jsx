import React from 'react';
import { Button } from 'react-bootstrap';
import { Fa } from '../../components';


/**
 * Simple crud button to display a button with an 'ADD' label and open the new form
 * It also supports pagination
 */
export default class CrudAddButton extends React.Component {

	constructor(props) {
		super(props);
		this.openNewForm = this.openNewForm.bind(this);
	}

	openNewForm() {
		this.props.controller.openForm();
	}

	render() {
		const props = Object.assign({}, this.props, { onClick: this.openNewForm });

		return (
			<Button {...props}>
				<Fa icon="plus-circle" />{__('action.add')}
			</Button>
			);
	}
}

CrudAddButton.propTypes = {
	controller: React.PropTypes.object.isRequired
};

CrudAddButton.defaultProps = {
	className: 'pull-right'
};
