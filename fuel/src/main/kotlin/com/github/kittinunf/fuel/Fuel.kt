package com.github.kittinunf.fuel

import com.github.kittinunf.fuel.core.Manager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Request

class Fuel {
    interface PathStringConvertible {
        val path: String
    }

    interface RequestConvertible {
        val request: Request
    }

    companion object {
        //convenience methods
        //get
        @JvmStatic @JvmOverloads
        fun get(path: String, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.GET, path, parameters)
        }

        @JvmStatic @JvmOverloads
        fun get(convertible: PathStringConvertible, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.GET, convertible, parameters)
        }

        //post
        @JvmStatic @JvmOverloads
        fun post(path: String, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.POST, path, parameters)
        }

        @JvmStatic @JvmOverloads
        fun post(convertible: PathStringConvertible, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.POST, convertible, parameters)
        }

        //put
        @JvmStatic @JvmOverloads
        fun put(path: String, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.PUT, path, parameters)
        }

        @JvmStatic @JvmOverloads
        fun put(convertible: PathStringConvertible, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.PUT, convertible, parameters)
        }

        //delete
        @JvmStatic @JvmOverloads
        fun delete(path: String, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.DELETE, path, parameters)
        }

        @JvmStatic @JvmOverloads
        fun delete(convertible: PathStringConvertible, parameters: List<Pair<String, Any?>>? = null): Request {
            return request(Method.DELETE, convertible, parameters)
        }

        //download
        @JvmStatic @JvmOverloads
        fun download(path: String, parameters: List<Pair<String, Any?>>? = null): Request {
            return Manager.instance.download(path, parameters)
        }

        @JvmStatic @JvmOverloads
        fun download(convertible: PathStringConvertible, parameters: List<Pair<String, Any?>>? = null): Request {
            return Manager.instance.download(convertible, parameters)
        }

        //upload
        @JvmStatic @JvmOverloads
        fun upload(path: String, method: Method = Method.POST, parameters: List<Pair<String, Any?>>? = null): Request {
            return Manager.instance.upload(path, method, parameters)
        }

        @JvmStatic @JvmOverloads
        fun upload(convertible: PathStringConvertible, method: Method = Method.POST, parameters: List<Pair<String, Any?>>? = null): Request {
            return Manager.instance.upload(convertible, method, parameters)
        }

        //request
        private fun request(method: Method, path: String, parameters: List<Pair<String, Any?>>? = null): Request {
            return Manager.instance.request(method, path, parameters)
        }

        private fun request(method: Method, convertible: PathStringConvertible, parameters: List<Pair<String, Any?>>? = null): Request {
            return Manager.instance.request(method, convertible, parameters)
        }

        @JvmStatic
        fun request(convertible: RequestConvertible): Request {
            return Manager.instance.request(convertible)
        }

    }

}

@JvmOverloads
fun String.httpGet(parameters: List<Pair<String, Any?>>? = null): Request {
    return Fuel.get(this, parameters)
}

@JvmOverloads
fun Fuel.PathStringConvertible.httpGet(parameter: List<Pair<String, Any?>>? = null): Request {
    return Fuel.get(this, parameter)
}

@JvmOverloads
fun String.httpPost(parameters: List<Pair<String, Any?>>? = null): Request {
    return Fuel.post(this, parameters)
}

@JvmOverloads
fun Fuel.PathStringConvertible.httpPost(parameter: List<Pair<String, Any?>>? = null): Request {
    return Fuel.post(this, parameter)
}

@JvmOverloads
fun String.httpPut(parameters: List<Pair<String, Any?>>? = null): Request {
    return Fuel.put(this, parameters)
}

@JvmOverloads
fun Fuel.PathStringConvertible.httpPut(parameter: List<Pair<String, Any?>>? = null): Request {
    return Fuel.put(this, parameter)
}

@JvmOverloads
fun String.httpDelete(parameters: List<Pair<String, Any?>>? = null): Request {
    return Fuel.delete(this, parameters)
}

@JvmOverloads
fun Fuel.PathStringConvertible.httpDelete(parameter: List<Pair<String, Any?>>? = null): Request {
    return Fuel.delete(this, parameter)
}

@JvmOverloads
fun String.httpDownload(parameter: List<Pair<String, Any?>>? = null): Request {
    return Fuel.download(this, parameter)
}

@JvmOverloads
fun Fuel.PathStringConvertible.httpDownload(parameter: List<Pair<String, Any?>>? = null): Request {
    return Fuel.download(this, parameter)
}

@JvmOverloads
fun String.httpUpload(method: Method = Method.POST, parameters: List<Pair<String, Any?>>? = null): Request {
    return Fuel.upload(this, method, parameters)
}

@JvmOverloads
fun Fuel.PathStringConvertible.httpUpload(method: Method = Method.POST, parameters: List<Pair<String, Any?>>? = null): Request {
    return Fuel.upload(this, method, parameters)
}
