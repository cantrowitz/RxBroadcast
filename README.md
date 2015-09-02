# RxBroadcast
Reactive `Broadcast` and `LocalBroadcast` for Android.

# Usage
#### System Broadcast
`Observable<Intent> = RxBroadcast.fromBroadcast(context, intentFilter);`

#### LocalBroadcast
`Observable<Intent> = RxBroadcast.fromLocalBroadcast(context, intentFilter);`

# Unregistering
Be sure to unregistering your receiver, this is typically done in the `Activity.onStop()`, by simply unsubscribing from your earlier subscription.

`Subscription subscription = RxBroadcast.fromLocalBroadcast(context, intentFilter).subscribe(...);`
...
```
  @Override
  protected void onStop() {
    super.onStop();

    if (subscription != null) {
      subscription.unsubscribe();
    }
  }
```

License
-------

    Copyright 2015 Adam Cantrowitz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
