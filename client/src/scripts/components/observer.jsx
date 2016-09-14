import React from 'react';
import { app } from '../core/app';

/**
 * Observer is a wrapper component that will automatically register itself to
 * receive app events, notifying the child component using the handleEvent method
 * @param Comp the component wrapped by the observer
 * @param events the event or list of events to listen to
 */
export default function observer(Comp, events) {

    class Observer extends React.Component {

        constructor(props) {
            super(props);
            this.handleEvents = this.handleEvents.bind(this);
        }

        componentWillMount() {
            app.add(this.handleEvents);
        }

        componentWillUnmount() {
            // remove registration of the component
            app.remove(this.handleEvents);
        }

        /**
         * Called on every application event
         */
        handleEvents(evt, data) {
            if (!this.refs.comp) {
                return;
            }

            // check if event must be dispatched to the child component
            let dispatch = true;
            if (events) {
                dispatch = Array.isArray(events) ? events.indexOf(evt) >= 0 : evt === events;
            }

            if (dispatch) {
                // dispatch it to the child component
                this.refs.comp.handleEvent(evt, data);
            }
        }

        render() {
            return <Comp ref="comp" {...this.props} />;
        }
    }

    return Observer;
}
