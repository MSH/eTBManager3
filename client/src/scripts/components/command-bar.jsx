import React from 'react';
import { NavItem, Collapse } from 'react-bootstrap';
import Fa from './fa';

import './command-bar.less';

export default class CommandBar extends React.Component {

	constructor(props) {
		super(props);
		this.clickItem = this.clickItem.bind(this);
	}

	componentWillMount() {
		this.state = { show: true };
	}

	clickItem(item) {
		return () => {
			if (item.onClick) {
				item.onClick(item);
			} else {
				this.setState({ item: item === this.state.item ? null : item });
			}
		};
	}

	renderItem(item, index) {
		if (item.node) {
			return item.node;
		}

		// if empty should not return null
		if (item.visible === false) {
			return null;
		}

		return (
			<li key={index} role="presentation">
			<a onClick={this.clickItem(item)}>
				<Fa icon={item.icon}/>{item.title}
			</a>
			{
				item.submenu &&
				<Collapse in={item === this.state.item}>
					<ul className="cmd-bar-sub nav">
						{
							item.submenu.map((cmd, i) => {
								// if empty should not return null
								if (cmd.visible === false) {
									return null;
								}

								return (
									<NavItem key={cmd.key ? cmd.key : i} onClick={this.clickItem(cmd)}>
										{cmd.icon && <Fa icon={cmd.icon} />}
										{cmd.title}
									</NavItem>
								);
							})
						}
					</ul>
				</Collapse>
			}
			</li>
			);
	}

	render() {
		const cmds = this.props.commands;
		if (!cmds) {
			return null;
		}

		return (
			<ul className="cmd-bar nav">
			{
				cmds.map((item, index) => this.renderItem(item, index))
			}
			</ul>
			);
	}
}

CommandBar.propTypes = {
	commands: React.PropTypes.array
};
