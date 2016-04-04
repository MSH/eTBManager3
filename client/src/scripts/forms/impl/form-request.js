
import FormUtils from '../form-utils';


/**
 * Collect requests from controls that need to be updated and
 * request just once to the server
 * @param  {array} snapshots The schema snapshot of the form
 * @return {Prmise}      A promise that will be resolved when request is completed
 */
export function requestServer(snapshots) {
	const lst = [];

	// create the requests
	snapshots.forEach(item => {
		const req = item.comp.serverRequest(item.snapshot, item.prev);
		if (req) {
			lst.push({
				id: item.id,
				cmd: req.cmd,
				params: req.params
			});
		}
	});

	if (lst.length === 0) {
		return Promise.resolve({});
	}

	return Promise.resolve(FormUtils.serverRequest(lst));
}
