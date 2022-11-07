let config = {
  mode: 'production',
  resolve: {
    modules: [
      "node_modules"
    ]
  },
  plugins: [],
  module: {
    rules: []
  }
};

// entry
config.entry = {
    main: ["C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\js\\packages\\fullStackWithKtor\\kotlin-dce\\fullStackWithKtor.js"]
};

config.output = {
    path: "C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\distributions",
    filename: (chunkData) => {
        return chunkData.chunk.name === 'main'
            ? "fullStackWithKtor.js"
            : "fullStackWithKtor-[name].js";
    },
    library: "fullStackWithKtor",
    libraryTarget: "umd",
    globalObject: "this"
};

// resolve modules
config.resolve.modules.unshift("C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\js\\packages\\fullStackWithKtor\\kotlin-dce")

// source maps
config.module.rules.push({
        test: /\.js$/,
        use: ["source-map-loader"],
        enforce: "pre"
});
config.devtool = 'source-map';
config.ignoreWarnings = [/Failed to parse source map/]

// Report progress to console
// noinspection JSUnnecessarySemicolon
;(function(config) {
    const webpack = require('webpack');
    const handler = (percentage, message, ...args) => {
        const p = percentage * 100;
        let msg = `${Math.trunc(p / 10)}${Math.trunc(p % 10)}% ${message} ${args.join(' ')}`;
        msg = msg.replace("C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\js", '');;
        console.log(msg);
    };

    config.plugins.push(new webpack.ProgressPlugin(handler))
})(config);

// KotlinWebpackCssRule[css]
;(function(config) {
            const use = [{
    loader: 'style-loader',
    options: {}
},{
    loader: 'css-loader',
    options: {}
}]
config.module.rules.push({
    test: /\.css$/,
    use: use,
    exclude: undefined,
    include: undefined,
})
})(config);

// noinspection JSUnnecessarySemicolon
;(function(config) {
    const tcErrorPlugin = require('kotlin-test-js-runner/tc-log-error-webpack');
    config.plugins.push(new tcErrorPlugin())
    config.stats = config.stats || {}
    Object.assign(config.stats, config.stats, {
        warnings: false,
        errors: false
    })
})(config);
// save evaluated config file
;(function(config) {
    const util = require('util');
    const fs = require('fs');
    const evaluatedConfig = util.inspect(config, {showHidden: false, depth: null, compact: false});
    fs.writeFile("C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\reports\\webpack\\fullStackWithKtor\\webpack.config.evaluated.js", evaluatedConfig, function (err) {});
})(config);

module.exports = config
