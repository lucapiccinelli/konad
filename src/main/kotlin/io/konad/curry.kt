package io.konad

fun <A, B, R> ((A, B) -> R).curry() = { a: A -> { b: B -> this(a, b) } }
fun <A, B, C, R> ((A, B, C) -> R).curry() = { a: A -> { b: B -> { c: C -> this(a, b, c) } } }
fun <A, B, C, D, R> ((A, B, C, D) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> this(a, b, c, d) } } } }
fun <A, B, C, D, E, R> ((A, B, C, D, E) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> {e: E -> this(a, b, c, d, e) } } } } }
fun <A, B, C, D, E, F, R> ((A, B, C, D, E, F) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> {e: E -> {f: F -> this(a, b, c, d, e, f) } } } } } }
fun <A, B, C, D, E, F, G, R> ((A, B, C, D, E, F, G) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> {e: E -> { f: F -> {g: G -> this(a, b, c, d, e, f, g) } } } } } } }
fun <A, B, C, D, E, F, G, H, R> ((A, B, C, D, E, F, G, H) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> {e: E -> { f: F -> { g: G -> { h: H -> this(a, b, c, d, e, f, g, h) } } } } } } } }
fun <A, B, C, D, E, F, G, H, I, R> ((A, B, C, D, E, F, G, H, I) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> { e: E -> { f: F -> { g: G -> { h: H -> { i: I ->this(a, b, c, d, e, f, g, h, i) } } } } } } } } }
fun <A, B, C, D, E, F, G, H, I, L, R> ((A, B, C, D, E, F, G, H, I, L) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> { e: E -> { f: F -> { g: G -> { h: H -> { i: I -> { l: L ->this(a, b, c, d, e, f, g, h, i, l) } } } } } } } } } }