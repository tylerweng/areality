const path = require('path');
const fs = require('fs');

const nodeModules = {};
fs.readdirSync('../node_modules')
  .filter(x => ['.bin'].indexOf(x) === -1)
  .forEach(mod => nodeModules[mod] = 'commonjs ' + mod);

module.exports = {
  context: __dirname,
  entry: "./app.js",
  output: {
    path: __dirname,
    filename: "bundle.js",
  },
  externals: nodeModules,
  module: {
    loaders: [
      {
        test: [/\.js$/],
        exclude: /node_modules/,
        loader: 'babel-loader',
        query: {
          presets: ["es2015"]
        }
      }
    ]
  },
  target: 'node',
  devtool: 'source-maps',
  resolve: {
    extensions: [".js", "*"]
  }
};
