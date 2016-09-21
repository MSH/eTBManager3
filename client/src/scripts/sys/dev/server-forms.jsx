import React from 'react';
import { Card, RemoteForm, FormDialog } from '../../components/index';
import { Button, ButtonToolbar } from 'react-bootstrap';
import { server } from '../../commons/server';

export default class ShowMessage extends React.Component {

    constructor(props) {
        super(props);
        this.click = this.click.bind(this);
        this.click2 = this.click2.bind(this);
        this.click3 = this.click3.bind(this);
        this.state = {};
    }

    click() {
        // using a path
        this.setState({ remotePath1: '/api/test/form' });
    }

    click2() {
        // using a function that will return a prosise with form data
        this.setState({
            remotePath1: () => server.get('/api/test/form')
        });
    }

    click3() {
        // using a function that will return a prosise with form data
        this.setState({
            remotePath2: () => server.get('/api/test/form/readonly/9f74407c-4c66-11e6-89fa-594b936a82f9')
        });
    }

    render() {
        const remotePath1 = this.state.remotePath1;
        const remotePath2 = this.state.remotePath2;

        return (
            <div>
                <Card title="Server forms example">
                    <ButtonToolbar>
                        <Button bsStyle="primary" onClick={this.click} >{'Get it'}</Button>
                        <Button bsStyle="primary" onClick={this.click2}>{'Get it 2'}</Button>
                        <Button bsStyle="primary" onClick={this.click3}>{'Get it readonly'}</Button>
                    </ButtonToolbar>
                    {
                        remotePath1 &&
                        <FormDialog
                            remotePath={remotePath1} />
                    }
                </Card>
                {
                    remotePath2 &&
                    <Card title="ReadOnly">
                        <RemoteForm
                            remotePath={remotePath2}
                            readOnly />
                    </Card>
                }
            </div>
            );
    }
}

ShowMessage.propTypes = {

};
