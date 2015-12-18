
var request = require('supertest'),
	agent = request.agent('http://localhost:8080');

agent.get('/api/sys/info')
.expect(200)
.end((err, res) => {
	console.log('ERR = ', err);
	console.log('RES = ', res.body);
});