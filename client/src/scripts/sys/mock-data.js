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

	return { name: name, gender: gender, age: age };
}


export function generateCaseNumber() {
	return Math.round((Math.random() * 10000) + 10000) + '-' + Math.round(Math.random() * 10);
}


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
		return this._event('update', id);
	}

	query(qry) {
		return this._event('query', qry);
	}
}
