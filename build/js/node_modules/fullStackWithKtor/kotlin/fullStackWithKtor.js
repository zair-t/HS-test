(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'fullStackWithKtor'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'fullStackWithKtor'.");
    }
    root.fullStackWithKtor = factory(typeof fullStackWithKtor === 'undefined' ? {} : fullStackWithKtor, kotlin);
  }
}(this, function (_, Kotlin) {
  'use strict';
  var split = Kotlin.kotlin.text.split_ip8yn$;
  var equals = Kotlin.equals;
  var throwCCE = Kotlin.throwCCE;
  var Unit = Kotlin.kotlin.Unit;
  var ensureNotNull = Kotlin.ensureNotNull;
  function main$lambda$lambda(it) {
    var tmp$, tmp$_0;
    var div = Kotlin.isType(tmp$ = document.getElementById('history'), HTMLDivElement) ? tmp$ : throwCCE();
    div.innerHTML = div.innerHTML + '<br>' + (typeof (tmp$_0 = it.data) === 'string' ? tmp$_0 : throwCCE());
    return Unit;
  }
  function main$lambda$lambda_0(closure$ws) {
    return function (it) {
      var tmp$;
      var message = (Kotlin.isType(tmp$ = document.getElementById('message'), HTMLInputElement) ? tmp$ : throwCCE()).value;
      closure$ws.send(message);
      return Unit;
    };
  }
  function main$lambda$lambda_1(closure$ws) {
    return function (it) {
      closure$ws.send('cleaning');
      return Unit;
    };
  }
  function main() {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    var path = split(document.URL, ['/']);
    if (path.size >= 2 && equals(path.get_za3lpa$(path.size - 2 | 0), 'chats')) {
      (Kotlin.isType(tmp$ = document.getElementById('clear'), HTMLButtonElement) ? tmp$ : throwCCE()).innerText = 'CLEAR CHAT';
      (Kotlin.isType(tmp$_0 = document.getElementById('send'), HTMLInputElement) ? tmp$_0 : throwCCE()).value = 'SEND MESSAGE';
    }
    var name = ensureNotNull(document.getElementsByTagName('h1')[0]).innerHTML;
    var $receiver = new WebSocket('ws://localhost:8080/main/chats/' + name);
    $receiver.onmessage = main$lambda$lambda;
    var ws = $receiver;
    (Kotlin.isType(tmp$_1 = document.getElementById('send'), HTMLInputElement) ? tmp$_1 : throwCCE()).onclick = main$lambda$lambda_0(ws);
    var $receiver_0 = Kotlin.isType(tmp$_2 = document.getElementById('clear'), HTMLButtonElement) ? tmp$_2 : throwCCE();
    $receiver_0.value = 'Clear';
    $receiver_0.onclick = main$lambda$lambda_1(ws);
  }
  _.main = main;
  main();
  Kotlin.defineModule('fullStackWithKtor', _);
  return _;
}));

//# sourceMappingURL=fullStackWithKtor.js.map
