(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin', 'react', 'kotlin-react-dom', 'kotlin-csstype', 'kotlin-emotion', 'kotlin-react-core', 'kotlin-react'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'), require('react'), require('kotlin-react-dom'), require('kotlin-csstype'), require('kotlin-emotion'), require('kotlin-react-core'), require('kotlin-react'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'fullStackWithKtor'.");
    }
    if (typeof react === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'react' was not found. Please, check whether 'react' is loaded prior to 'fullStackWithKtor'.");
    }
    if (typeof this['kotlin-react-dom'] === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'kotlin-react-dom' was not found. Please, check whether 'kotlin-react-dom' is loaded prior to 'fullStackWithKtor'.");
    }
    if (typeof this['kotlin-csstype'] === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'kotlin-csstype' was not found. Please, check whether 'kotlin-csstype' is loaded prior to 'fullStackWithKtor'.");
    }
    if (typeof this['kotlin-emotion'] === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'kotlin-emotion' was not found. Please, check whether 'kotlin-emotion' is loaded prior to 'fullStackWithKtor'.");
    }
    if (typeof this['kotlin-react-core'] === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'kotlin-react-core' was not found. Please, check whether 'kotlin-react-core' is loaded prior to 'fullStackWithKtor'.");
    }
    if (typeof this['kotlin-react'] === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'kotlin-react' was not found. Please, check whether 'kotlin-react' is loaded prior to 'fullStackWithKtor'.");
    }
    root.fullStackWithKtor = factory(typeof fullStackWithKtor === 'undefined' ? {} : fullStackWithKtor, kotlin, react, this['kotlin-react-dom'], this['kotlin-csstype'], this['kotlin-emotion'], this['kotlin-react-core'], this['kotlin-react']);
  }
}(this, function (_, Kotlin, $module$react, $module$kotlin_react_dom, $module$kotlin_csstype, $module$kotlin_emotion, $module$kotlin_react_core, $module$kotlin_react) {
  'use strict';
  var $$importsForInline$$ = _.$$importsForInline$$ || (_.$$importsForInline$$ = {});
  var ensureNotNull = Kotlin.ensureNotNull;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var Unit = Kotlin.kotlin.Unit;
  var throwCCE = Kotlin.throwCCE;
  var useState = $module$react.useState;
  var html = $module$kotlin_react_dom.react.dom.html;
  var InputType$text = /*union*/{button: 'button', checkbox: 'checkbox', color: 'color', date: 'date', datetimeLocal: 'datetime-local', email: 'email', file: 'file', hidden: 'hidden', image: 'image', month: 'month', number: 'number', password: 'password', radio: 'radio', range: 'range', reset: 'reset', search: 'search', submit: 'submit', tel: 'tel', text: 'text', time: 'time', url: 'url', week: 'week'}/*union*/.text;
  var FC = $module$kotlin_react.react.FC_4y0n3r$;
  var css = $module$kotlin_emotion.$$importsForInline$$['@emotion/css'].css;
  function main$lambda$lambda(it) {
    println('connected');
    println(document.URL);
    return Unit;
  }
  function main$lambda$lambda_0(it) {
    var tmp$, tmp$_0, tmp$_1;
    println('We got a message: ' + (typeof (tmp$ = it.data) === 'string' ? tmp$ : throwCCE()));
    var div = Kotlin.isType(tmp$_0 = document.getElementById('history'), HTMLDivElement) ? tmp$_0 : throwCCE();
    div.innerHTML = div.innerHTML + '<br>' + (typeof (tmp$_1 = it.data) === 'string' ? tmp$_1 : throwCCE());
    return Unit;
  }
  function main$lambda$lambda_1(closure$ws) {
    return function (it) {
      var tmp$;
      var message = (Kotlin.isType(tmp$ = document.getElementById('message'), HTMLInputElement) ? tmp$ : throwCCE()).value;
      closure$ws.send(message);
      return Unit;
    };
  }
  function main$lambda$lambda_2(closure$ws) {
    return function (it) {
      closure$ws.send('cleaning');
      return Unit;
    };
  }
  function main() {
    var tmp$, tmp$_0;
    var name = ensureNotNull(document.getElementsByTagName('h1')[0]).innerHTML.toString();
    var $receiver = new WebSocket('ws://localhost:8080/main/chats/' + name);
    $receiver.onopen = main$lambda$lambda;
    $receiver.onmessage = main$lambda$lambda_0;
    var ws = $receiver;
    (Kotlin.isType(tmp$ = document.getElementById('send'), HTMLInputElement) ? tmp$ : throwCCE()).onclick = main$lambda$lambda_1(ws);
    var $receiver_0 = Kotlin.isType(tmp$_0 = document.getElementById('clear'), HTMLButtonElement) ? tmp$_0 : throwCCE();
    $receiver_0.value = 'Clear';
    $receiver_0.onclick = main$lambda$lambda_2(ws);
  }
  function Welcome$lambda$lambda(closure$name) {
    return function ($receiver) {
      var $receiver_0 = {};
      $receiver_0.padding = (5).toString() + 'px';
      $receiver_0.backgroundColor = 'rgb(' + 8 + ',' + 97 + ',' + 22 + ')';
      $receiver_0.color = 'rgb(' + 56 + ',' + 246 + ',' + 137 + ')';
      $receiver.className = css($receiver_0);
      $receiver.unaryPlus_pdl1vz$('Hello, ' + closure$name[0]);
      return Unit;
    };
  }
  function Welcome$lambda$lambda$lambda(closure$name) {
    return function (event) {
      var $this = closure$name;
      var value = event.target.value;
      $this[1](value);
      return Unit;
    };
  }
  function Welcome$lambda$lambda_0(closure$name) {
    return function ($receiver) {
      var $receiver_0 = {};
      $receiver_0.marginTop = (5).toString() + 'px';
      $receiver_0.marginBottom = (5).toString() + 'px';
      $receiver_0.fontSize = (14).toString() + 'px';
      $receiver.className = css($receiver_0);
      $receiver.type = InputType$text;
      $receiver.value = closure$name[0];
      $receiver.onChange = Welcome$lambda$lambda$lambda(closure$name);
      return Unit;
    };
  }
  function Welcome$lambda($receiver, props) {
    var name = useState(props.name);
    $receiver.invoke_gax9jq$(html.ReactHTML.div, Welcome$lambda$lambda(name));
    $receiver.invoke_gax9jq$(html.ReactHTML.input, Welcome$lambda$lambda_0(name));
    return Unit;
  }
  var Welcome;
  _.main = main;
  $$importsForInline$$['kotlin-emotion'] = $module$kotlin_emotion;
  $$importsForInline$$['kotlin-react-core'] = $module$kotlin_react_core;
  Welcome = FC(Welcome$lambda);
  main();
  return _;
})); //# sourceMappingURL=fullStackWithKtor.js.map

//# sourceMappingURL=fullStackWithKtor.js.map
