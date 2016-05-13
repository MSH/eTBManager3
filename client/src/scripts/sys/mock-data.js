/**
 * Generate mock data for displaying purposes
 * @return {[type]} [description]
 */


export function generateName() {
	const gender = Math.random() >= 0.5 ? 'MALE' : 'FEMALE';

	const name = (gender === 'MALE' ?
		firstNamesMale[randonIndex(firstNamesMale.length)] :
		firstNamesFemale[randonIndex(firstNamesFemale.length)]) + ' ' +
		lastNames[randonIndex(lastNames.length)];

	const age = Math.round((Math.random() * 40) + 10);

	return {
		name: name,
		gender: gender,
		age: age,
		id: Math.round(Math.random() * 10000000)
	};
}


export function generateCaseNumber() {
	return Math.round((Math.random() * 10000) + 10000) + '-' + Math.round(Math.random() * 10);
}


const tags = [
	{
		id: '123456-1',
		name: 'Not on treatment',
		type: 'userdef'
	},
	{
		id: '123456-2',
		name: 'On treatment',
		type: 'userdef'
	},
	{
		id: '123456-3',
		name: 'Closed cases',
		type: 'warn'
	},
	{
		id: '123456-4',
		name: 'DR-TB with no resistance',
		type: 'danger'
	},
	{
		id: '123456-5',
		name: 'TB with resistance',
		type: 'danger'
	}
];


// TEMPORARY -> CASE DATA USED FOR PROTOTYPING
const mockTbCase = {
	patient: {
		name: 'Jim Morrison',
		gender: 'MALE',
		birthDate: new Date(1970, 1, 1),
		motherName: 'Maria Morrison'
	},
	diagnosisDate: new Date(2016, 5, 1),
	age: 35,
	notificationUnit: {
		id: '123456-123355',
		name: 'Centro de Referência Prof Hélio Fraga',
		adminUnit: {
			p1: {
				id: '1234-1234',
				name: 'RJ'
			},
			p2: {
				id: '12333-333',
				name: 'Rio de Janeiro'
			}
		}
	},
	notifAddress: {
		adminUnit: {
			p1: {
				id: '1234-1234',
				name: 'RJ'
			},
			p2: {
				id: '12333-333',
				name: 'Rio de Janeiro'
			}
		}
	},
	tags: tags,
	adverseReactions: [
		{
			id: '4848484-1',
			adverseReaction: { id: 1, name: 'Adverse Reaction 1' },
			medicine: 'Terizidon',
			month: 2
		},
		{
			id: '4848484-2',
			adverseReaction: { id: 2, name: 'Adverse Reaction 2' },
			medicine: 'Isoniazid',
			month: 5
		},
		{
			id: '4848484-3',
			adverseReaction: { id: 1, name: 'Adverse Reaction 3' },
			medicine: 'Amicacin',
			month: 8
		}
	],
	comments: [
		{
			id: '123456-12',
			user: {
				id: '12312312',
				name: 'Bruce Dickinson'
			},
			group: 'contacts',
			date: new Date(),
			comment: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum'
		},
		{
			id: '123456-11',
			user: {
				id: '12312312',
				name: 'Iron Maiden'
			},
			group: 'contacts',
			date: new Date(),
			comment: 'Contact Rubens Smith refused interview and moved out to another address'
		}
	]
};


export { mockTbCase };

export function mockCrud() {
	return new MockCrud();
}

function randonIndex(size) {
	return Math.round(Math.random() * size);
}

const firstNamesMale = [
	'Airton',
	'Augusto',
	'Abu',
	'Abdul',
	'Albert',
	'Antony',
	'Anderson',
	'Arnaldo',
	'Andre',
	'Alan',
	'Alexandre',
	'Alex',
	'Aleksey',
	'Arin',
	'Arnauld',
	'Agnaldo',
	'Albert',
	'Alberto',
	'Abaeté',
	'Abdão',
	'Abdias',
	'Abel',
	'Ademar',
	'Barnabas',
	'Barnaby',
	'Barton',
	'Bernard',
	'Bevis',
	'Bond',
	'Booth',
	'Brick',
	'Brier',
	'Brigham',
	'Brinley',
	'Baltasar',
	'Baren',
	'Beto',
	'Benedito',
	'Bernardo',
	'Benjamin',
	'Becan',
	'Bruno',
	'Brandon',
	'Biafra',
	'Baltazar',
	'Bartolomeu',
	'Batista',
	'Belisario',
	'Benjamin',
	'Bonifácio',
	'Bruce',
	'Carter',
	'Cecil',
	'Cedric',
	'Charles',
	'Chick',
	'Chilton',
	'Chip',
	'Christian',
	'Christopher',
	'Cicero',
	'Cláudio',
	'Clenildo',
	'Carlos',
	'Caetano',
	'Clayton',
	'Claude',
	'Conrado',
	'Constantino',
	'Constantin',
	'Caio',
	'Clarence',
	'Clay',
	'Clayland',
	'Clayton',
	'Cleave',
	'Clem',
	'Clemens',
	'Clement',
	'Cliff',
	'Clifford',
	'Clint',
	'Clinton',
	'Dado',
	'Daniel',
	'Dilson',
	'David',
	'Dean',
	'Dean',
	'Diamond',
	'Dixon',
	'Duff',
	'Ebenezer',
	'Edgar',
	'Edward',
	'Edwin',
	'Eliah',
	'Eliezer',
	'Iuri',
	'Yuri',
	'Jean',
	'Mohamed',
	'Nuno',
	'Norberto',
	'Nilo',
	'Nicholas',
	'Noel',
	'Osmar',
	'Oswald',
	'Paulo',
	'Pedro',
	'Peter',
	'Paul',
	'Phillip',
	'Patrick',
	'Raymond',
	'Ricardo',
	'Roosevelt',
	'Rennan',
	'Renato',
	'Raul',
	'Ramon',
	'Rachid',
	'Ringo',
	'Sergey',
	'Samuel',
	'Sandro',
	'Silvester',
	'Steffano',
	'Steve',
	'Will',
	'Xavier'
];

const firstNamesFemale = [
	'Alynn',
	'Abbey',
	'Aaron',
	'Abbie',
	'Abbigail',
	'Abby',
	'Abigail',
	'Abigayle',
	'Abilene',
	'Abiranna',
	'Ada',
	'Adabel',
	'Addison',
	'Addsyn',
	'Adela',
	'Albertina',
	'Agnes',
	'Andreia',
	'Angela',
	'Arinan',
	'Ana Maria',
	'Ana Lúcia',
	'Ana',
	'Ameely',
	'Alice',
	'Aline',
	'Arminda',
	'Benedita',
	'Bruna',
	'Beatriz',
	'Barbara',
	'Bianca',
	'Berenice',
	'Bruna',
	'Clara',
	'Célia',
	'Carolina',
	'Carol',
	'Carla',
	'Clarice',
	'Daniele',
	'Diana',
	'Débora',
	'Emily',
	'Esmeralda',
	'Elisabeth',
	'Flávia',
	'Fátima',
	'Fabiana',
	'Fernanda',
	'Gilda',
	'Helena',
	'Iraci',
	'Iná',
	'Jocilaine',
	'Maria',
	'Maria Antônia',
	'Manoela',
	'Michele',
	'Mel',
	'Melissa',
	'Nair',
	'Karla',
	'Keli',
	'Kimberly',
	'Keila',
	'Lara',
	'Laura',
	'Margareth',
	'Mabel',
	'Madaline',
	'Madelene',
	'Madeline',
	'Madelyn',
	'Nataly',
	'Noelma',
	'Otaviana',
	'Paula',
	'Patrícia',
	'Paloma',
	'Paris',
	'Regina',
	'Roberta',
	'Rogéria',
	'Raquel',
	'Rebeca',
	'Raimunda',
	'Romana',
	'Rose',
	'RaDonna',
	'Rae',
	'Raechel',
	'Sylvia',
	'Sara',
	'Telma',
	'Tara',
	'Uma',
	'Vada',
	'Val',
	'Valarie',
	'Velma',
	'Valéria',
	'Velma',
	'Vanity',
	'Velia',
	'Velma',
	'Quilma',
	'Qbilah',
	'Queen',
	'Queena',
	'Queenie',
	'Quentina',
	'Quiana',
	'Quinn',
	'Karen',
	'Kelly'
];

const lastNames = [
	'Antunes',
	'Abbott',
	'Adamczyk',
	'Agnaudo',
	'Araujo',
	'Apple',
	'da Silva',
	'Debrieux',
	'Batista',
	'Bolivar',
	'Brasil',
	'Bottino',
	'Blue',
	'Bachman',
	'Bailey',
	'Brick',
	'Dianno',
	'DAvona',
	'Dmarco',
	'Da neen',
	'Da wan',
	'Daava',
	'Dafne',
	'Dagian',
	'Dahlia',
	'Daija',
	'Dantas',
	'Dickinson',
	'da Costa',
	'Copala',
	'Cortez',
	'Costeau',
	'da Costa Saldanha',
	'Caliva',
	'Callaghan',
	'Dutra',
	'Boon',
	'Bernardes',
	'Busht',
	'Dabrowski',
	'Daecher',
	'Edwards',
	'Eisenhower',
	'Ford',
	'Farias',
	'Fernandes',
	'Fernandez',
	'Forest',
	'Fancher',
	'Fantauzzo',
	'Gallet',
	'Gomes',
	'Gump',
	'Greenhill',
	'Greece',
	'Green',
	'Gray',
	'Garcia',
	'Gusmão',
	'Gabriell',
	'Gao',
	'Gardenia',
	'Galena',
	'Gallo',
	'Garcia',
	'Hahn',
	'Haley',
	'Himan',
	'Hill',
	'Hutson',
	'Highway',
	'Julia',
	'Jordan',
	'Jackson',
	'Johnson',
	'Silveira'
];


/**
 * Mock class to simulate a crud object
 */
class MockCrud {
	on(func) {
		this.handler = func;
		return this;
	}

	_event(evt, data, defaultRes) {
		return new Promise(resolve => {
			const res = this.handler(evt, data);
			setTimeout(() => {
				if (res) {
					resolve(res);
				} else {
					resolve(defaultRes);
				}
			}, 500);
		});
	}

	get(id) {
		return this._event('get', id);
	}

	getEdit(id) {
		return this._event('get-edit', id);
	}

	create(req) {
		return this._event('create', req, { success: true, result: '123456' });
	}

	update(id) {
		return this._event('update', id, { success: true, result: id });
	}

	query(qry) {
		return this._event('query', qry);
	}

	delete(id) {
		return this._event('delete', id, { success: true, result: id });
	}
}
