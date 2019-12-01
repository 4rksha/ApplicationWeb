let express = require('express');
let cors = require('cors');
let app = express();
app.use(cors());

app.use(express.static('www'));

let server = app.listen(80,function () {

    console.log('Server listening');

});