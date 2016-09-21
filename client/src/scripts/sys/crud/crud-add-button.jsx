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

    openNewForm(key) {
        this.props.controller.openNewForm(key);
    }

    getBtnTitle() {
        return <span><Fa icon="plus-circle" />{__('action.add')}</span>;
    }

    /**
     * Return the options of a popup menu, if available
     * @return {[type]} [description]
     */
    popupMenu() {
        const controller = this.props.controller;

        const lst = controller.getEditors();

        // if it is a single editor, return null
        if (!lst) {
            return null;
        }

        // return the list of menu options
        const options = lst.map(item =>
                <MenuItem key={item.key} eventKey={item.key} onSelect={this.openNewForm}>
                    {item.label}
                </MenuItem>
                );

        return (
            <DropdownButton id="optMenu"
                title={this.getBtnTitle()}
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

        delete props.controller;

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
    controller: React.PropTypes.object.isRequired
};

CrudAddButton.defaultProps = {
    className: 'pull-right'
};
