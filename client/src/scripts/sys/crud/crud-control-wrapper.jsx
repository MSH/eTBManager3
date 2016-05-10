import React from 'react';

export default function controlWrapper(Component) {

	class CrudControl extends React.Component {

		componentWillMount() {
			// register the handler
			const self = this;
			const handler = this.props.controller.on((evt, data) => {
				self.refs.ctrl.eventHandler(evt, data);
			});
			this.setState({ handler: handler });
		}

		componentWillUnmount() {
			// remove registration
			this.props.controller.removeListener(this.state.handler);
		}

		render() {
			return (
				<Component ref="ctrl" {...this.props} />
				);
		}

	}

	CrudControl.propTypes = {
		controller: React.PropTypes.object
	};

	return CrudControl;
}
