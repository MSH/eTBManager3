import React from 'react';
import { Button, MenuItem, DropdownButton } from 'react-bootstrap';
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

	getBtnTitle() {
		return <span><Fa icon="plus-circle" />{__('action.add')}</span>;
	}

	/**
	 * Return the options of a popup menu, if available
	 * @return {[type]} [description]
	 */
	popupMenu() {
		const sc = this.props.schema;

		// if it is a single editor, return null
		if (!sc || !sc.editors) {
			return null;
		}

		// return the list of menu options
		const options = Object.keys(sc.editors).map(key => {
			// get title to be displayed
			const title = sc.editors[key].title;

			return (
				<MenuItem key={key} eventKey={key}>
					{title ? title : key}
				</MenuItem>
				);
		});

		return (
			<DropdownButton id="optMenu" bsSize="small" pullRight
				title={this.getTitle()}
				onSelect={this.newMenuClick}>
				{
					options
				}
			</DropdownButton>
		);
	}


	render() {
		const ppMenu = this.popupMenu();
		if (ppMenu) {
			return ppMenu;
		}

		const props = Object.assign({}, this.props, { onClick: this.openNewForm });

		return ppMenu ? ppMenu : (
			<Button {...props}>
				{
					this.getBtnTitle()
				}
			</Button>
			);
	}
}

CrudAddButton.propTypes = {
	controller: React.PropTypes.object.isRequired,
	schema: React.PropTypes.object
};

CrudAddButton.defaultProps = {
	className: 'pull-right'
};
