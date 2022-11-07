{
  mode: 'production',
  resolve: {
    modules: [
      'C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\js\\packages\\fullStackWithKtor\\kotlin-dce',
      'node_modules'
    ]
  },
  plugins: [
    ProgressPlugin {
      profile: false,
      handler: [Function: handler],
      modulesCount: 5000,
      dependenciesCount: 10000,
      showEntries: true,
      showModules: true,
      showDependencies: true,
      showActiveModules: false,
      percentBy: undefined
    },
    TeamCityErrorPlugin {}
  ],
  module: {
    rules: [
      {
        test: /\.js$/,
        use: [
          'source-map-loader'
        ],
        enforce: 'pre'
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: 'style-loader',
            options: {}
          },
          {
            loader: 'css-loader',
            options: {}
          }
        ],
        exclude: undefined,
        include: undefined
      }
    ]
  },
  entry: {
    main: [
      'C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\js\\packages\\fullStackWithKtor\\kotlin-dce\\fullStackWithKtor.js'
    ]
  },
  output: {
    path: 'C:\\Users\\Zair\\IdeaProjects\\fullStackWithKtor\\build\\distributions',
    filename: [Function: filename],
    library: 'fullStackWithKtor',
    libraryTarget: 'umd',
    globalObject: 'this'
  },
  devtool: 'source-map',
  ignoreWarnings: [
    /Failed to parse source map/
  ],
  stats: {
    warnings: false,
    errors: false
  }
}