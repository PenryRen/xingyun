let Ssh_URL = 'http://192.168.1.228:8089/index';

if (process.env.NODE_ENV === 'development') {
  Ssh_URL= "http://127.0.0.1:8089/index"
}

export {
  Ssh_URL
}
