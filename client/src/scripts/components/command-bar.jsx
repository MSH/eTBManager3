import React from 'react';
import { NavItem } from 'react-bootstrap';
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
			<div className="cmd-bar nav">
				<div className="title">{__('form.options')}</div>
			{
				cmds.map((item, index) => this.renderItem(item, index))
			}
			</div>
			);
	}
}

CommandBar.propTypes = {
	commands: React.PropTypes.array
};
