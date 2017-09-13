from app import app

if __name__ == "__main__":
    app.debug=True
    app.config.from_object("config.BaseConfig")
    app.run(host="0.0.0.0")
