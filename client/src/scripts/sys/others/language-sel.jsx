import React from 'react';
import { Modal, Button, Nav, NavItem } from 'react-bootstrap';
import { Fa } from '../../components';
import { app } from '../../core/app';


export default class LanguageSel extends React.Component {

	constructor(props) {
		super(props);

		this.close = this.close.bind(this);
	}

	componentWillMount() {
		const self = this;
		const handler = app.add(evt => {
			if (evt === 'change-lang') {
				self.setState({ show: true });
			}
		});

		this.state = { handler: handler };
	}

	componentWillUnmount() {
		app.remove(this.state.handler);
	}

	close() {
		this.setState({ show: null });
	}

	changeLang(lang) {
		return () => {
			app.setLang(lang.id);
		};
	}

	render() {
		if (!this.state.show) {
			return null;
		}

		const langs = app.getState().app.languages;
		const sellang = app.getLang();

		return (
			<Modal show={this.state.show} >
				<Modal.Header closeButton>
					<Modal.Title id="contained-modal-title">{__('changelang')}</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Nav bsStyle="pills" stacked>
						{
							langs.map(lang => (
								<NavItem bsStyle="pills" key={lang.id} onClick={this.changeLang(lang)}>
								<Fa icon={lang.id === sellang ? 'circle' : ''} className="text-success"/>
								<span className={lang.id === sellang ? 'text-success' : 'text-muted'}>
								{lang.name}
								</span>
								</NavItem>
							))
						}
					</Nav>
				</Modal.Body>
				<Modal.Footer>
					<Button bsStyle="primary" onClick={this.close}>{__('action.cancel')}</Button>
				</Modal.Footer>
				</Modal>
			);
	}
}

LanguageSel.propTypes = {

};
