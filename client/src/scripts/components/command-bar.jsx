import React from 'react';
import { Nav, NavItem } from 'react-bootstrap';
import Fa from './fa';

export default class CommandBar extends React.Component {

	renderItem(item, index) {
		if (item.node) {
			return item.node;
		}

		return (
			<NavItem key={index} onClick={item.onClick}>
				<Fa icon={item.icon}/>{item.label}
			</NavItem>
			);
	}

	render() {
		const cmds = this.props.commands;
		if (!cmds) {
			return null;
		}

		return (
			<Nav className="cmd-bar">
				<div className="title">{__('form.options')}</div>
			{
				cmds.map((item, index) => this.renderItem(item, index))
			}
			</Nav>
			);
	}
}

CommandBar.propTypes = {
	commands: React.PropTypes.array
};
