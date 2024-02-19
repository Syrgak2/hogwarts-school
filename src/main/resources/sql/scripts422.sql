
--скрипты кода один человек может владеть несколькими машинами
--и одной машиной могут владеть несколько человек
--ManyToMany
CREATE TABLE person (
    person_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    has_license BOOLEAN NOT NULL
);

CREATE TABLE cars (
    car_id SERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL
);

CREATE TABLE ownership (
    ownership_id SERIAL PRIMARY KEY,
    person_id INTEGER REFERENCES person(person_id),
    car_id INTEGER REFERENCES cars(car_id),
    CONSTRAINT unique_ownership UNIQUE (person_id, car_id)
);


----скрипты кода один человек может владеть одной машиной
  --и одной машиной могут владеть несколько человек
  --OneToMany

  CREATE TABLE Person (
      person_id SERIAL PRIMARY KEY,
      name VARCHAR(100),
      age INT,
      has_license BOOLEAN,
      car_id INT,
      FOREIGN KEY (car_id) REFERENCES Cars(car_id)
  );

  CREATE TABLE Cars (
      car_id SERIAL PRIMARY KEY,
      brand VARCHAR(100),
      model VARCHAR(100),
      price DECIMAL(10, 2)
  );
